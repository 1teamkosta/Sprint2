const statusBadge = document.querySelector('#user-detail-status');
const actionButton = document.querySelector('#user-detail-status-action');
const feedbackMessage = document.querySelector('#user-detail-feedback');

// 초기 상태 가져오기 (값이 없으면 기본값 'active')
let currentStatus = document.body.dataset.userStatus || 'active';

// 화면의 배지와 버튼 모양을 현재 상태에 맞게 바꾸는 함수
function updateStatusView() {
    if (currentStatus === 'active') {
        // '활동' 상태일 때 보여줄 화면
        statusBadge.textContent = '활동';
        statusBadge.className = 'badge user-detail-active';

        actionButton.textContent = '정지';
        actionButton.className = 'btn user-detail-suspend';
        actionButton.setAttribute('aria-label', '회원 활동 정지');
    } else {
        // '정지' 상태일 때 보여줄 화면
        statusBadge.textContent = '정지';
        statusBadge.className = 'badge user-detail-suspended';

        actionButton.textContent = '활동';
        actionButton.className = 'btn user-detail-activate';
        actionButton.setAttribute('aria-label', '회원 활동 재개');
    }

    actionButton.disabled = false;
}

// 버튼을 클릭했을 때 실행되는 동작
actionButton.addEventListener('click', function () {
    if (currentStatus === 'active') {
        const isConfirmed = window.confirm('이 회원의 활동을 정지할까요?');
        if (!isConfirmed) return;

        currentStatus = 'banned';
    } else {
        const isConfirmed = window.confirm('이 회원의 활동을 재개할까요?');
        if (!isConfirmed) return;

        currentStatus = 'active';
    }

    // 바뀐 상태로 화면 다시 그리기
    updateStatusView();

    // 안내 문구 보여주기
    feedbackMessage.textContent = '현재 화면에서 회원 상태를 변경했습니다. 실제 저장은 서버 연동 후 가능합니다.';
    feedbackMessage.hidden = false;
});

// 페이지가 처음 열릴 때 한 번 실행
updateStatusView();
