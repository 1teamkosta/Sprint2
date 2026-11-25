/**
 * 상세 정보와 참여 모임은 UserController가 Thymeleaf 모델로 전달한다.
 * 이 파일은 회원 상태 버튼의 화면 시연 동작만 담당하며 HTML 페이지를 fetch하지 않는다.
 */
const userDetailAction = document.querySelector('#user-detail-status-action');
const userDetailFeedback = document.querySelector('#user-detail-feedback');
let userDetailStatus = document.body.dataset.userStatus || 'active';

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

userDetailAction.addEventListener('click', function () {
    const active = userDetailStatus === 'active';
    if (!window.confirm(active ? '이 회원의 활동을 정지할까요?' : '이 회원의 활동을 재개할까요?')) return;
    userDetailStatus = active ? 'suspended' : 'active';
    renderUserDetailStatus();
    userDetailFeedback.textContent = '현재 화면에서 회원 상태를 변경했습니다. 실제 저장은 서버 연동 후 가능합니다.';
    userDetailFeedback.hidden = false;
});

renderUserDetailStatus();