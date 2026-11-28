// 공지 목록을 조회하고 검색·정렬·페이지 이동·삭제를 처리한다.
const noticeQuery = new URLSearchParams(window.location.search);

function changeNoticeQuery(changes) {
    Object.entries(changes).forEach(function ([name, value]) {
        if (name === 'page' && value != null) {
            value = Math.max(0, Number(value) - 1);
        }

        if (name === 'keyword' && value != null) {
            value = value.trim();
        }

        if (value == null || (name === 'keyword' && value === '')) {
            noticeQuery.delete(name);
        } else {
            noticeQuery.set(name, value);
        }
    });

    return loadNotices();
}

function loadNotices() {
    const count = document.querySelector('#notice-result-count');
    const url = noticeQuery.toString()
        ? `/admin/notices?${noticeQuery.toString()}`
        : '/admin/notices';

    return fetch(url).then(function (response) {
        if (!response.ok) {
            throw new Error(`공지 목록 조회에 실패했습니다. (${response.status})`);
        }
        return response.json();
    }).then(function (result) {
        renderNotices(result.content || []);

        const total = Number(result.totalElements) || 0;
        count.textContent = `검색 결과 ${total}건`;
        renderPagination({
            containerSelector: '#notice-pagination',
            pagesSelector: '#notice-pagination .pagination',
            currentPage: (Number(result.number) || 0) + 1,
            pageCount: Number(result.totalPages) || 0,
            totalCount: total,
            onPageChange: function (page) {
                return changeNoticeQuery({ page });
            }
        });

        const pageUrl = new URL(window.location.href);
        pageUrl.search = noticeQuery.toString();
        history.replaceState(null, '', pageUrl);
    }).catch(function (error) {
        count.textContent = error.message;
        console.error(error);
    });
}

function renderNotices(notices) {
    const tbody = document.querySelector('#notice-list');
    const fragment = document.createDocumentFragment();

    if (!notices.length) {
        const emptyRow = document.createElement('tr');
        const emptyCell = document.createElement('td');
        emptyCell.colSpan = 4;
        emptyCell.className = 'py-5 text-secondary';
        emptyCell.textContent = '조건에 맞는 공지가 없습니다.';
        emptyRow.append(emptyCell);
        fragment.append(emptyRow);
    }

    notices.forEach(function (notice) {
        const row = document.createElement('tr');
        const checkCell = document.createElement('td');
        checkCell.className = 'list-selection-column';

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'form-check-input list-selection-input';
        checkbox.name = 'noticeIds';
        checkbox.value = notice.noticeNumber;
        checkbox.setAttribute('aria-label', `공지 ${notice.noticeNumber}번 선택`);
        checkCell.append(checkbox);

        const numberCell = document.createElement('td');
        numberCell.textContent = notice.noticeNumber;

        const titleCell = document.createElement('td');
        titleCell.className = 'notice-title';
        titleCell.title = notice.title || '';
        const link = document.createElement('a');
        link.className = 'list-detail-link';
        link.href = `/admin/noticeDetailView/${encodeURIComponent(notice.noticeNumber)}`;
        link.textContent = notice.title || '';
        titleCell.append(link);

        const dateCell = document.createElement('td');
        const time = document.createElement('time');
        time.textContent = notice.inDate || '';
        dateCell.append(time);

        row.append(checkCell, numberCell, titleCell, dateCell);
        fragment.append(row);
    });

    tbody.replaceChildren(fragment);
}

const noticeList = setupList({
    prefix: 'notice',
    tableSelector: '.notice-table',
    onQueryChange: changeNoticeQuery
});

const noticeSearchType = document.querySelector('#notice-search-type');
if (noticeSearchType) {
    noticeSearchType.value = noticeQuery.get('searchBy') || 'title';
}
document.querySelector('#notice-keyword').value = noticeQuery.get('keyword') || '';

document.querySelector('#notice-delete-selected').addEventListener('click', function () {
    const numbers = Array.from(
        document.querySelectorAll('#notice-list .list-selection-input:checked')
    ).map(function (checkbox) { return checkbox.value; });

    if (!numbers.length) {
        alert('삭제할 공지를 선택해 주세요.');
        return;
    }
    if (!confirm(`선택한 공지 ${numbers.length}건을 삭제할까요?`)) return;

    Promise.all(numbers.map(function (number) {
        return fetch(`/admin/notice/${encodeURIComponent(number)}`, {
            method: 'DELETE'
        }).then(function (response) {
            if (!response.ok) {
                throw new Error(`공지 ${number}번 삭제에 실패했습니다. (${response.status})`);
            }
        });
    })).then(function () {
        noticeList.resetSelection();
        return changeNoticeQuery({ page: 1 });
    }).catch(function (error) {
        alert(error.message);
        console.error(error);
        loadNotices();
    });
});

window.addEventListener('popstate', function () {
    Array.from(noticeQuery.keys()).forEach(function (name) { noticeQuery.delete(name); });
    new URLSearchParams(window.location.search).forEach(function (value, name) {
        noticeQuery.set(name, value);
    });
    document.querySelector('#notice-keyword').value = noticeQuery.get('keyword') || '';
    loadNotices();
});

loadNotices();
