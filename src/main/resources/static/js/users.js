// 회원 관리 화면도 공통 목록 기능을 사용한다.
// setupList({
//     prefix: 'user',
//     tableSelector: '.user-table',
//     idField: 'userId',
//     exactFields: ['userId']
// });
const bulkStatusMap = {
    active: 1,
    suspended: 0
};

const userBulkButton = document.querySelector('[data-bulk-action="user"]');
const userBulkSelect = document.querySelector('#user-bulk-status');

if (userBulkButton && userBulkSelect) {
    userBulkButton.addEventListener('click', async function () {
        const selectedIds = Array.from(
            document.querySelectorAll('#user-list input[name="userIds"]:checked')
        ).map(function (checkbox) {
            return Number(checkbox.value);
        });

        const selectedAction = userBulkSelect.value;

        if (selectedIds.length === 0) {
            alert('상태를 변경할 회원을 선택해 주세요.');
            return;
        }

        if (!selectedAction) {
            alert('변경할 상태를 선택해 주세요.');
            return;
        }

        const targetStatus = bulkStatusMap[selectedAction];

        try {
            const response = await fetch('/admin/users/status', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    accountNumbers: selectedIds,
                    status: targetStatus
                })
            });

            if (!response.ok) {
                throw new Error('회원 상태 변경에 실패했습니다.');
            }

            userBulkSelect.value = '';
            await loadUsers(new URLSearchParams(window.location.search));
        } catch (error) {
            showUserError(error);
        }
    });
}




const statusFilterMap = {
    all: null,
    active: 1,
    banned: 0
};

const statusFilterDetails = document.querySelector('#user-status-filter');
const statusFilterButtons = Array.from(
    document.querySelectorAll('[data-status-filter]')
);

statusFilterButtons.forEach(function (button) {
    button.addEventListener('click', async function () {
        const filterKey = button.dataset.statusFilter;
        const filterValue = statusFilterMap[filterKey] ?? null;

        await changeUserQuery({
            page: 1,
            filterByStatus: filterValue
        });

        statusFilterButtons.forEach(function (item) {
            const isSelected = item === button;
            item.setAttribute('aria-pressed', String(isSelected));
            item.classList.toggle('active', isSelected);
        });

        if (statusFilterDetails) {
            statusFilterDetails.open = false;
        }
    });
});





function changeUserQuery(changes) {
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

    return loadUsers(params).catch(function (error) {
        showUserError(error);
        throw error;
    });
}

const userList = setupList({
    prefix: 'user',
    tableSelector: '.user-table',
    onQueryChange: changeUserQuery
});

async function loadUsers(params) {
    const response = await fetch(`/admin/users?${params.toString()}`);
    if (!response.ok) {
        throw new Error('회원 목록 조회에 실패했습니다.');
    }

    const result = await response.json();

    renderUsers(result.list);
    userList.resetSelection();

    document.querySelector('#user-result-count').textContent =
        `검색 결과 ${result.total}건`;

    renderPagination({
        containerSelector: '#user-pagination',
        pagesSelector: '#user-pagination .pagination',
        currentPage: Number(result.pageNum) || 1,
        pageCount: Number(result.pages) || 0,
        totalCount: Number(result.total) || 0,
        onPageChange: function (page) {
            return changeUserQuery({ page });
        }
    });

    const viewUrl = new URL(window.location.href);
    viewUrl.search = params.toString();
    history.pushState(null, '', viewUrl);
}

function showUserError(error) {
    document.querySelector('#user-result-count').textContent = error.message;
}

function renderUsers(users) {
    const tbody = document.querySelector('#user-list');
    const emptyRow = document.querySelector('#user-empty');
    const fragment = document.createDocumentFragment();

    users.forEach(function (user) {
        const row = document.createElement('tr');  const checkTd = document.createElement('td');
        checkTd.className = 'list-selection-column';
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'form-check-input list-selection-input';
        checkbox.name = 'userIds';
        checkbox.value = user.accountNumber;
        checkbox.setAttribute('aria-label', `회원 ${user.accountNumber}번 선택`);
        checkTd.append(checkbox);

        const idTd = document.createElement('td');
        idTd.textContent = user.accountNumber;

        // const statusTd = document.createElement('td');
        // const statusSpan = document.createElement('span');
        // statusSpan.textContent = user.status;
        // statusTd.append(statusSpan);
        const statusTd = document.createElement('td');
        const statusBadge = document.createElement('span');
        const isActive = Number(user.status) === 1;

        statusBadge.className = 'badge';
        statusBadge.classList.add(isActive ? 'user-status-active' : 'user-status-banned');
        statusBadge.textContent = isActive ? '활동' : '정지';
        statusTd.append(statusBadge);

        const nicknameTd = document.createElement('td');
        nicknameTd.className = 'fw-medium';

        const link = document.createElement('a');
        link.className = 'list-detail-link';
        link.href = `/admin/userDetailView/${encodeURIComponent(user.accountNumber)}`;
        link.textContent = user.nickname;

        nicknameTd.append(link);



        // const nicknameSpan = document.createElement('span');
        // nicknameSpan.textContent = user.nickname;
        // nicknameTd.append(nicknameSpan);

        const emailTd = document.createElement('td');
        emailTd.textContent = user.email;

        const popularityTd = document.createElement('td');
        popularityTd.textContent = user.popularity;

        const inDateTd = document.createElement('td');
        const timeEl = document.createElement('time');
        timeEl.textContent = user.inDate;
        inDateTd.append(timeEl);



        row.append(checkTd, idTd, statusTd, nicknameTd, emailTd, popularityTd, inDateTd);
        fragment.append(row);
    });

    // 회원 테이블 HTML 구조에 맞게 td와 값 채우기

    if (emptyRow) {
        emptyRow.hidden = users.length > 0;
        fragment.append(emptyRow);
    }

    tbody.replaceChildren(fragment);
}
