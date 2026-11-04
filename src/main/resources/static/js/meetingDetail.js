/**
 * 목록 HTML의 더미 데이터를 ID로 찾아 상세를 구성한다.
 * 서버 연동 시 모임 상세 API로 교체한다. 내용·인원·참여자는 화면 확인용 데이터다.
 * 비공개 변경은 현재 화면의 메모리에만 반영하며 목록·DB·브라우저 저장소에는 저장하지 않는다.
 */
const meetingHideButton = document.querySelector('#meeting-detail-hide');
const meetingStatusBadge = document.querySelector('#meeting-detail-status');
const meetingDetailFeedback = document.querySelector('#meeting-detail-feedback');
let meetingDetailIsPublic = false;
function renderMeetingVisibility() {
    meetingStatusBadge.textContent = meetingDetailIsPublic ? '공개' : '비공개';
    meetingStatusBadge.className = `badge meeting-detail-${meetingDetailIsPublic ? 'visible' : 'hidden'} mb-3`;
    meetingHideButton.disabled = !meetingDetailIsPublic;
}
async function loadMeetingDetail() {
    const meetingId = new URLSearchParams(window.location.search).get('id') || '1';
    try {
        const response = await fetch('meetings.html');
        if (!response.ok) throw new Error('모임 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const row = Array.from(listDocument.querySelectorAll('tbody tr[data-meeting-type]')).find(function (item) {
            return item.querySelector('[data-field="meetingId"]').textContent.trim() === meetingId;
        });
        if (!row) throw new Error('해당 모임을 찾을 수 없습니다.');
        const title = row.querySelector('[data-field="title"]').textContent.trim();
        const festival = row.querySelector('[data-field="festival"] a');
        const host = row.querySelector('[data-field="host"]').textContent.trim();
        const purpose = row.dataset.meetingType === 'food' ? '음식메이트' : '축제메이트';
        document.title = `${title} | 축제메이트`;
        document.querySelector('#meeting-detail-title').textContent = title;
        const festivalLink = document.querySelector('#meeting-detail-festival');
        festivalLink.textContent = festival.textContent.trim();
        festivalLink.href = festival.getAttribute('href');
        document.querySelector('#meeting-detail-purpose').textContent = purpose;
        const hostSource = row.querySelector('[data-field="host"] a');
        const hostLink = document.querySelector('#meeting-detail-host');
        hostLink.textContent = host;
        hostLink.href = hostSource.getAttribute('href');
        // 참여자는 실제 회원 상세로 연결할 수 있는 더미 회원을 사용하며 개설자와 겹치지 않게 한다.
        const hostId = new URL(hostLink.href, document.baseURI).searchParams.get('id');
        const demoMember = hostId === '1' ? { id: '2', nickname: '이음식' } : { id: '1', nickname: '김축제' };
        const memberLink = document.querySelector('#meeting-detail-member');
        memberLink.textContent = demoMember.nickname;
        memberLink.href = `../users/userDetail.html?id=${demoMember.id}`;
        const created = row.querySelector('[data-sort-field="createdAt"] time');
        const createdTarget = document.querySelector('#meeting-detail-created');
        createdTarget.textContent = created.textContent;
        createdTarget.setAttribute('datetime', created.getAttribute('datetime'));
        // 모임일은 축제 일정에 맞춘 시연용 값이며, 서버 연동 시 모임의 실제 날짜로 교체한다.
        const demoMeetingDates = [
            '2026-11-02', '2026-11-03', '2026-11-04', '2026-11-05',
            '2026-11-06', '2026-11-07', '2026-11-08', '2026-11-09',
            '2026-11-10', '2026-11-11', '2026-11-12'
        ];
        const meetingDate = demoMeetingDates[Number(meetingId) - 1];
        const meetingDateTarget = document.querySelector('#meeting-detail-meeting-date');
        meetingDateTarget.textContent = meetingDate ? meetingDate.replaceAll('-', '.') : '미정';
        if (meetingDate) meetingDateTarget.setAttribute('datetime', meetingDate);
        document.querySelector('#meeting-detail-description').textContent = purpose === '음식메이트'
            ? `${festival.textContent.trim()}에서 다양한 먹거리를 함께 나눠 먹을 분을 구합니다.\n먹고 싶은 메뉴를 이야기하고 편하게 함께 즐겨요!`
            : `${festival.textContent.trim()}에 함께 갈 축제메이트를 구합니다.\n공연과 체험 프로그램을 함께 즐기며 좋은 추억을 만들어 보세요!`;
        meetingDetailIsPublic = row.dataset.status === 'visible';
        renderMeetingVisibility();
        document.querySelector('#meeting-detail-card').hidden = false;
        meetingDetailFeedback.hidden = true;
    } catch (error) {
        meetingDetailFeedback.textContent = '모임 정보를 표시할 수 없습니다. 목록으로 돌아가 다시 선택해 주세요.';
        console.error(error);
    }
}
meetingHideButton.addEventListener('click', function () {
    if (!meetingDetailIsPublic || !window.confirm('이 모임을 비공개로 변경할까요?')) return;
    meetingDetailIsPublic = false;
    renderMeetingVisibility();
    meetingDetailFeedback.textContent = '현재 화면에서 비공개로 변경했습니다. 실제 저장은 서버 연동 후 가능합니다.';
    meetingDetailFeedback.hidden = false;
});
loadMeetingDetail();
