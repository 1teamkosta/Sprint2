// 모임 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.meeting-title', 30);
shortenListText('.meeting-festival', 30);
shortenListText('.meeting-table [data-field="host"]', 5);

let selectedMeetingType = 'all';
const meetingList = setupList({
    prefix: 'meeting',
    tableSelector: '.meeting-table',
    idField: 'meetingId',
    extraFilter: function (row) {
        return selectedMeetingType === 'all' || row.dataset.meetingType === selectedMeetingType;
    }
});

// 탭은 모임 화면에서 관리하고, 공통 목록에 추가 조건만 전달한다.
// 검색·공개 상태·정렬 기준은 유지하며 탭을 바꾸면 1페이지부터 보여준다.
const meetingTabs = Array.from(document.querySelectorAll('[data-meeting-type-tab]'));
function selectMeetingType(type) {
    selectedMeetingType = type;
    meetingTabs.forEach(function (tab) {
        const active = tab.dataset.meetingTypeTab === type;
        tab.classList.toggle('active', active);
        tab.setAttribute('aria-selected', String(active));
        tab.tabIndex = active ? 0 : -1;
        if (active) document.querySelector('#meeting-tab-panel').setAttribute('aria-labelledby', tab.id);
    });
    meetingList.refresh();
}

meetingTabs.forEach(function (tab, index) {
    tab.addEventListener('click', function () { selectMeetingType(tab.dataset.meetingTypeTab); });
    tab.addEventListener('keydown', function (event) {
        if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return;
        event.preventDefault();
        const next = event.key === 'Home' ? 0 : event.key === 'End' ? meetingTabs.length - 1
            : (index + (event.key === 'ArrowRight' ? 1 : -1) + meetingTabs.length) % meetingTabs.length;
        meetingTabs[next].focus();
        selectMeetingType(meetingTabs[next].dataset.meetingTypeTab);
    });
});
