/**
 * 회원 목록의 더미 데이터로 상세를 채운다. 서버 연동 시 회원 상세 API로 교체한다.
 * 로그인 아이디·참여 모임 관계는 시연용 데이터이며 실제 회원 데이터가 아니다.
 * 상태 변경은 현재 화면에만 반영되고 새로고침 시 초기화된다.
 */
const userDetailAction = document.querySelector('#user-detail-status-action');
const userDetailFeedback = document.querySelector('#user-detail-feedback');
let userDetailStatus = null;
function renderUserDetailStatus() {
    const active = userDetailStatus === 'active';
    const badge = document.querySelector('#user-detail-status');
    badge.textContent = active ? '활동' : '정지';
    badge.className = `badge user-detail-${active ? 'active' : 'suspended'}`;
    userDetailAction.textContent = active ? '정지' : '활동';
    userDetailAction.className = active ? 'btn user-detail-suspend' : 'btn user-detail-activate';
    userDetailAction.disabled = false;
    userDetailAction.setAttribute('aria-label', active ? '회원 활동 정지' : '회원 활동 재개');
}
async function loadUserDetail() {
    const userId = new URLSearchParams(window.location.search).get('id') || '1';
    try {
        const response = await fetch('users.html');
        if (!response.ok) throw new Error('회원 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const row = Array.from(listDocument.querySelectorAll('tbody tr[data-status]')).find(function (item) {
            return item.querySelector('[data-field="userId"]').textContent.trim() === userId;
        });
        if (!row) throw new Error('해당 회원을 찾을 수 없습니다.');
        const nickname = row.querySelector('[data-field="nickname"]').textContent.trim();
        document.title = `${nickname} 회원 상세 | 축제메이트`;
        document.querySelector('#user-detail-number').textContent = userId;
        document.querySelector('#user-detail-login-id').textContent = `festival_user${userId.padStart(2, '0')}`;
        document.querySelector('#user-detail-nickname').textContent = nickname;
        document.querySelector('#user-detail-email').textContent = row.querySelector('[data-field="email"]').textContent.trim();
        document.querySelector('#user-detail-popularity').textContent = row.querySelector('[data-sort-field="popularity"]').textContent.trim();
        const joined = row.querySelector('[data-field="joinedAt"] time');
        const joinedTarget = document.querySelector('#user-detail-joined');
        joinedTarget.textContent = joined.textContent;
        joinedTarget.setAttribute('datetime', joined.getAttribute('datetime'));
        userDetailStatus = row.dataset.status;
        renderUserDetailStatus();
        document.querySelector('#user-detail-card').hidden = false;
        userDetailFeedback.hidden = true;
        await loadUserClubs(userId, nickname);
    } catch (error) {
        userDetailFeedback.textContent = '회원 정보를 표시할 수 없습니다. 목록으로 돌아가 다시 선택해 주세요.';
        userDetailFeedback.hidden = false;
        console.error(error);
    }
}
async function loadUserClubs(userId, nickname) {
    const feedback = document.querySelector('#user-clubs-feedback');
    const body = document.querySelector('#user-detail-clubs');
    try {
        const response = await fetch('../club/clubs.html');
        if (!response.ok) throw new Error('모임 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const rows = Array.from(listDocument.querySelectorAll('tbody tr[data-club-type]'));
        // 김축제(1번)는 페이지네이션 시연을 위해 전체 11개 모임에 참여한 것으로 연결한다.
        // 개설자 역할은 실제 목록의 닉네임으로 구분하고, 다른 회원은 일반 참여 하나를 연결한다.
        const hosted = rows.filter(function (row) { return row.querySelector('[data-field="host"]').textContent.trim() === nickname; });
        const otherClubs = rows.filter(function (row) { return !hosted.includes(row); });
        const joined = userId === '1'
            ? otherClubs
            : otherClubs.length ? [otherClubs[(Number(userId) - 1) % otherClubs.length]] : [];
        const memberships = [...hosted.map(function (row) { return { row, role: '개설자' }; }), ...joined.map(function (row) { return { row, role: '참여자' }; })];
        let currentPage = 1;
        // 전체 참여 내역을 10개씩 나눈다. 링크와 역할은 각 페이지를 그릴 때 그대로 유지한다.
        function renderMembershipPage() {
            const pageData = paginateItems(memberships, currentPage, 10);
            currentPage = pageData.currentPage;
            body.replaceChildren();
            pageData.items.forEach(function (membership) {
                const row = membership.row;
                const tr = document.createElement('tr');
                const id = row.querySelector('[data-field="clubId"]').textContent.trim();
                function cell(text, className = '') {
                    const td = document.createElement('td');
                    td.className = className;
                    td.textContent = text;
                    return td;
                }
                const festivalCell = cell('', 'text-center');
                const originalFestival = row.querySelector('[data-field="festival"] a');
                const festivalLink = document.createElement('a');
                festivalLink.textContent = originalFestival.textContent.trim();
                festivalLink.title = festivalLink.textContent;
                festivalLink.href = originalFestival.getAttribute('href');
                festivalCell.append(festivalLink);
                const titleCell = cell('', 'text-center');
                const clubLink = document.createElement('a');
                clubLink.textContent = row.querySelector('[data-field="title"]').textContent.trim();
                clubLink.title = clubLink.textContent;
                clubLink.href = `../club/clubDetail.html?id=${encodeURIComponent(id)}`;
                titleCell.append(clubLink);
                tr.append(cell(id), festivalCell, titleCell, cell(membership.role), cell(row.dataset.clubType === 'food' ? '음식메이트' : '축제메이트'));
                body.append(tr);
            });
            document.querySelector('#user-detail-club-count').textContent = `(${memberships.length}개)`;
            if (!memberships.length) {
                const tr = document.createElement('tr');
                const td = document.createElement('td');
                td.colSpan = 5;
                td.className = 'text-secondary py-4';
                td.textContent = '참여한 모임이 없습니다.';
                tr.append(td);
                body.append(tr);
            }
            renderPagination({
                containerSelector: '#user-detail-clubs-pagination',
                pagesSelector: '#user-detail-clubs-pages',
                currentPage,
                pageCount: pageData.pageCount,
                totalCount: pageData.totalCount,
                onPageChange: function (page) {
                    currentPage = page;
                    renderMembershipPage();
                }
            });
            // 10개 이하면 한 페이지이므로 페이지 버튼을 표시하지 않는다.
            document.querySelector('#user-detail-clubs-pagination').hidden = pageData.pageCount <= 1;
        }
        renderMembershipPage();
        feedback.hidden = true;
    } catch (error) {
        feedback.textContent = '참여 모임을 불러오지 못했습니다.';
        console.error(error);
    }
}
userDetailAction.addEventListener('click', function () {
    if (!userDetailStatus) return;
    const active = userDetailStatus === 'active';
    if (!window.confirm(active ? '이 회원의 활동을 정지할까요?' : '이 회원의 활동을 재개할까요?')) return;
    userDetailStatus = active ? 'suspended' : 'active';
    renderUserDetailStatus();
    userDetailFeedback.textContent = '현재 화면에서 회원 상태를 변경했습니다. 실제 저장은 서버 연동 후 가능합니다.';
    userDetailFeedback.hidden = false;
});
loadUserDetail();
