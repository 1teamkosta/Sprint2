/**
 * 상세 데이터는 ClubController가 Thymeleaf 모델로 전달한다.
 * 이 파일은 상세 화면의 임시 상태 표시만 관리하며 페이지를 fetch하지 않는다.
 */
const clubHideButton = document.querySelector('#club-detail-hide');
const clubStatusBadge = document.querySelector('#club-detail-status');
const clubDetailFeedback = document.querySelector('#club-detail-feedback');
let clubDetailIsPublic = document.body.dataset.clubPublic === 'true';

function renderClubVisibility() {
    clubStatusBadge.textContent = clubDetailIsPublic ? '공개' : '비공개';
    clubStatusBadge.className = `badge club-detail-${clubDetailIsPublic ? 'visible' : 'hidden'} mb-3`;
    clubHideButton.disabled = !clubDetailIsPublic;
}

clubHideButton.addEventListener('click', function () {
    if (!clubDetailIsPublic || !window.confirm('이 모임을 비공개로 변경할까요?')) return;
    clubDetailIsPublic = false;
    renderClubVisibility();
    clubDetailFeedback.textContent = '현재 화면에서 비공개로 변경했습니다. 실제 저장은 서버 연동 후 가능합니다.';
    clubDetailFeedback.hidden = false;
});

renderClubVisibility();