// 모임 관리 말줄임과 공통 목록 설정
shortenListText('.club-title', 30);
shortenListText('.club-festival', 30);
shortenListText('.club-table [data-field="nickname"]', 5);

// 모임목적 탭 이동시 clubTabs.forEach에서 정한 조건 변경값 반영 + 공백 검색시 1페이지로 이동 + 조회실패시 오류 표시
function changeClubQuery(changes) {
    if (typeof changes.keyword === 'string'
        && changes.keyword.trim() === '') {
        changes = {
            ...changes,
            page: 1,
            searchBy: null,
            keyword: null
        };
    }

    const params = new URLSearchParams(window.location.search);

    Object.entries(changes).forEach(function ([name, value]) {
        if (value == null) {
            params.delete(name);
        } else {
            params.set(name, value);
        }
    });

    return loadClubs(params).catch(function (error) {
        showClubError(error);
        throw error;
    });
}

const bulkButton = document.querySelector('[data-bulk-action="club"]');

// 상태변경
bulkButton.addEventListener('click', async function () {
    const selectedNumbers = Array.from(
        document.querySelectorAll(
            '#club-list .list-selection-input:checked'
        )
    ).map(function (checkbox) {
        return checkbox.value;
    });

    const selectedStatus =
        document.querySelector('#club-bulk-status').value;

    if (selectedNumbers.length === 0 || selectedStatus === '') {
        alert('모임과 변경할 상태를 선택해 주세요.');
        return;
    }

    const params = new URLSearchParams();

    selectedNumbers.forEach(function (number) {
        params.append('clubNumbers', number);
    });

    params.set('state', selectedStatus === 'visible' ? 1 : 0);

    try {
        const response = await fetch(
            `${bulkButton.dataset.updateUrl}?${params.toString()}`,
            { method: 'PUT' }
        );

        if (!response.ok) {
            throw new Error('상태 변경에 실패했습니다.');
        }

        // 현재 검색·필터·페이지 조건으로 목록 다시 조회
        await changeClubQuery({});
    } catch (error) {
        showClubError(error);
    }
});

const purposeByType = {
    all: null,
    food: '음식메이트',
    festival: '축제메이트'
};

const clubTabs = Array.from(document.querySelectorAll('[data-club-type-tab]'));

//어떤 조건을 바꿀지 정함
clubTabs.forEach(function (tab) {
    tab.addEventListener('click', async function () {
        try {
            await changeClubQuery({
                page: 1,
                filterByPurpose: purposeByType[tab.dataset.clubTypeTab]
            });

            clubTabs.forEach(function (item) {
                const active = item === tab;

                item.classList.toggle('active', active);
                item.setAttribute('aria-selected', String(active));
                item.tabIndex = active ? 0 : -1;
            });

            document.querySelector('#club-tab-panel')
                .setAttribute('aria-labelledby', tab.id);
        } catch (error) {
            showClubError(error);
        }
    });
});

const clubList = setupList({
    prefix: 'club',
    tableSelector: '.club-table',
    onQueryChange: changeClubQuery
});

async function loadClubs(params) {
    const response = await fetch(`/admin/clubs?${params.toString()}`);
    if (!response.ok) {
        throw new Error('모임 목록 조회에 실패했습니다.');
    }

    const result = await response.json();

    renderClubs(result.list);
    clubList.resetSelection();

    document.querySelector('#club-result-count').textContent =
        `검색 결과 ${result.total}건`;

    renderPagination({
        containerSelector: '#club-pagination',
        pagesSelector: '#club-pagination .pagination',
        currentPage: result.pageNum,
        pageCount: result.pages,
        totalCount: result.total,
        onPageChange: function (page) {
            const nextParams = new URLSearchParams(params);
            nextParams.set('page', page);
            loadClubs(nextParams).catch(showClubError);
        }
    });

    const viewUrl = new URL(window.location.href);
    viewUrl.search = params.toString();
    history.pushState(null, '', viewUrl);
}

function showClubError(error) {
    document.querySelector('#club-result-count').textContent = error.message;
}

function renderClubs(clubs) {
    const tbody = document.querySelector('#club-list');
    const emptyRow = document.querySelector('#club-empty');
    const fragment = document.createDocumentFragment();

    clubs.forEach(function (club) {
        const row = document.createElement('tr');

        row.dataset.status = club.status === 1 ? 'visible' : 'hidden';
        row.dataset.clubType =
            club.purpose === '음식메이트' ? 'food' : 'festival';

        // 행의 기본 구조만 만들기
        row.innerHTML = `
            <td class="list-selection-column">
                <input class="form-check-input list-selection-input"
                       type="checkbox" name="clubIds">
            </td>
            <td data-field="clubId"></td>
            <td><span class="badge"></span></td>
            <td class="club-festival text-start" data-field="festival">
                <a class="list-detail-link"></a>
            </td>
            <td class="club-title fw-medium text-start" data-field="title">
                <a class="list-detail-link"></a>
            </td>
            <td class="club-purpose" data-field="purpose"></td>
            <td data-field="host">
                <a class="list-detail-link"></a>
            </td>
            <td data-sort-field="createdAt"><time></time></td>
        `;

        const checkbox = row.querySelector('input');
        checkbox.value = club.clubNumber;
        checkbox.setAttribute('aria-label', `모임 ${club.clubNumber}번 선택`);

        row.querySelector('[data-field="clubId"]').textContent =
            club.clubNumber;

        const badge = row.querySelector('.badge');
        badge.textContent = club.status === 1 ? '공개' : '비공개';
        badge.classList.add(
            club.status === 1 ? 'club-status-visible' : 'club-status-hidden'
        );

        const festivalLink = row.querySelector('[data-field="festival"] a');
        festivalLink.textContent = club.festivalName;
        festivalLink.href = `/admin/festival/${club.festivalNumber}`;

        const clubLink = row.querySelector('[data-field="title"] a');
        clubLink.textContent = club.clubName;
        clubLink.href = `/admin/clubDetailView/${club.clubNumber}`;

        row.querySelector('[data-field="purpose"]').textContent =
            club.purpose;

        const hostLink = row.querySelector('[data-field="host"] a');
        hostLink.textContent = club.nickname;
        hostLink.href = `/admin/userDetailView/${club.accountNumber}`;

        row.querySelector('time').textContent = club.createdAt;

        fragment.append(row);
    });

    emptyRow.hidden = clubs.length > 0;
    fragment.append(emptyRow);

    tbody.replaceChildren(fragment);


}



