const clubHideButton = document.querySelector('#club-detail-hide');
const clubStatusBadge = document.querySelector('#club-detail-status');
const clubDetailFeedback = document.querySelector('#club-detail-feedback');
let clubDetailIsPublic = document.body.dataset.clubPublic === 'true';

function renderClubVisibility() {
    clubStatusBadge.textContent = clubDetailIsPublic ? '공개' : '비공개';
    clubStatusBadge.className =
        `badge club-detail-${clubDetailIsPublic ? 'visible' : 'hidden'} mb-3`;

    clubHideButton.textContent =
        clubDetailIsPublic ? '비공개' : '공개';
}

clubHideButton.addEventListener('click', async function () {
    const nextState = clubDetailIsPublic ? 0 : 1;
    const label = nextState === 1 ? '공개' : '비공개';

    if (!window.confirm(`${label}로 변경할까요?`)) return;

    clubHideButton.disabled = true;

    try {
        const url = new URL(clubHideButton.dataset.updateUrl, window.location.origin);
        url.searchParams.set('state', nextState);

        const response = await fetch(url, {
            method: 'PUT'
        });

        if (!response.ok) {
            throw new Error('상태 변경에 실패했습니다.');
        }

        clubDetailIsPublic = nextState === 1;
        renderClubVisibility();

        clubDetailFeedback.textContent = `${label}로 변경했습니다.`;
    } catch (error) {
        clubDetailFeedback.textContent = error.message;
    } finally {
        clubHideButton.disabled = false;
        clubDetailFeedback.hidden = false;
    }
});

renderClubVisibility();