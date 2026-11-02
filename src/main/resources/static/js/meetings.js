// 모임 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.meeting-title', 30);
shortenListText('.meeting-festival', 30);
shortenListText('.meeting-table [data-field="host"]', 5);

setupList({
    prefix: 'meeting',
    tableSelector: '.meeting-table',
    idField: 'meetingId'
});
