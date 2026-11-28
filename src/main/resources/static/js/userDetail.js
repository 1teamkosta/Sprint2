const statusBadge = document.querySelector('#user-detail-status');
const actionButton = document.querySelector('#user-detail-status-action');
const feedbackMessage = document.querySelector('#user-detail-feedback');

let currentStatus = document.body.dataset.userStatus || 'active';

function getAccountNumber() {
    const numberText = document.querySelector('#user-detail-number')?.textContent?.trim();
    if (numberText) return Number(numberText);

    const matched = window.location.pathname.match(/\/(\d+)(?:\/)?$/);
    return matched ? Number(matched[1]) : null;
}

function updateStatusView() {
    if (!statusBadge || !actionButton) return;

    if (currentStatus === 'active') {
        statusBadge.textContent = '활동';
        statusBadge.className = 'badge user-detail-active';

        actionButton.textContent = '정지';
        actionButton.className = 'btn user-detail-suspend';
        actionButton.setAttribute('aria-label', '회원 활동 정지');
    } else {
        statusBadge.textContent = '정지';
        statusBadge.className = 'badge user-detail-suspended';

        actionButton.textContent = '활동';
        actionButton.className = 'btn user-detail-activate';
        actionButton.setAttribute('aria-label', '회원 활동 재개');
    }

    actionButton.disabled = false;
}

if (actionButton) {
    actionButton.addEventListener('click', async function () {
        const accountNumber = getAccountNumber();
        if (!accountNumber) {
            alert('회원 번호를 확인할 수 없습니다.');
            return;
        }

        const isCurrentlyActive = currentStatus === 'active';
        const confirmText = isCurrentlyActive
            ? '이 회원의 활동을 정지할까요?'
            : '이 회원의 활동을 재개할까요?';

        if (!window.confirm(confirmText)) return;

        const nextStatus = isCurrentlyActive ? 'banned' : 'active';
        const nextStatusCode = isCurrentlyActive ? 0 : 1;

        actionButton.disabled = true;

        try {
            const response = await fetch(`/admin/user/${encodeURIComponent(accountNumber)}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    status: nextStatusCode
                })
            });

            if (!response.ok) {
                throw new Error('회원 상태 변경에 실패했습니다.');
            }

            currentStatus = nextStatus;
            updateStatusView();

            if (feedbackMessage) {
                feedbackMessage.textContent = isCurrentlyActive
                    ? '회원 활동이 정지되었습니다.'
                    : '회원 활동이 재개되었습니다.';
                feedbackMessage.hidden = false;
            }
        } catch (error) {
            actionButton.disabled = false;
            alert(error.message || '상태 변경 중 오류가 발생했습니다.');
        }
    });
}

// --- 여기서부터 참여 모임(활동 내역) 목록 & 페이지네이션 추가 ---

function renderActivities(activities) {
    const tbody = document.querySelector('#user-detail-clubs');
    if (!tbody) return;

    const fragment = document.createDocumentFragment();

    activities.forEach(function (activity) {
        const row = document.createElement('tr');

        const clubNumTd = document.createElement('td');
        clubNumTd.textContent = activity.clubNumber;

        const festivalTd = document.createElement('td');
        if (activity.festivalNumber != null) {
            const festivalLink = document.createElement('a');
            festivalLink.href = `/admin/festivalDetailView/${encodeURIComponent(activity.festivalNumber)}`;
            festivalLink.textContent = activity.festivalName ?? '';
            festivalTd.append(festivalLink);
        } else {
            festivalTd.textContent = activity.festivalName ?? '';
        }


        const clubNameTd = document.createElement('td');
        if (activity.clubNumber != null) {
            const clubLink = document.createElement('a');
            clubLink.href = `/admin/clubDetailView/${encodeURIComponent(activity.clubNumber)}`;
            clubLink.textContent = activity.clubName ?? '';
            clubNameTd.append(clubLink);
        } else {
            clubNameTd.textContent = activity.clubName ?? '';
        }

        const roleTd = document.createElement('td');
        roleTd.textContent = Number(activity.attendeeRole) === 0 ? '개설자' : '참가자';

        const purposeTd = document.createElement('td');
        purposeTd.textContent = activity.purpose ?? '';

        row.append(clubNumTd, festivalTd, clubNameTd, roleTd, purposeTd);
        fragment.append(row);
    });

    tbody.replaceChildren(fragment);
}

async function loadUserActivities(page = 1) {
    const accountNumber = getAccountNumber();
    if (!accountNumber) return;

    const response = await fetch(
        `/admin/user/${encodeURIComponent(accountNumber)}/activities?page=${encodeURIComponent(page)}`
    );
    if (!response.ok) {
        throw new Error('참여 모임 목록 조회에 실패했습니다.');
    }

    const result = await response.json();
    renderActivities(result.list || []);

    if (typeof renderPagination === 'function') {
        renderPagination({
            containerSelector: '#user-detail-clubs-pagination',
            pagesSelector: '#user-detail-clubs-pages',
            currentPage: Number(result.pageNum) || 1,
            pageCount: Number(result.pages) || 0,
            totalCount: Number(result.total) || 0,
            onPageChange: function (nextPage) {
                return loadUserActivities(nextPage);
            }
        });
    }
}

updateStatusView();
loadUserActivities(1);
