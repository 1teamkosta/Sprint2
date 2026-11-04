// 로컬 날짜를 YYYY-MM-DD로 비교한다. 시작일과 종료일 당일은 모두 진행중이다.
const festivalToday = new Date();
const festivalTodayKey = `${festivalToday.getFullYear()}-${String(festivalToday.getMonth() + 1).padStart(2, '0')}-${String(festivalToday.getDate()).padStart(2, '0')}`;
document.querySelectorAll('.festival-table tbody tr[data-status]').forEach(function (row) {
    const dates = row.querySelectorAll('[data-sort-field="startDate"] time');
    const start = dates[0].getAttribute('datetime');
    const end = dates[1].getAttribute('datetime');
    const progress = festivalTodayKey < start ? 'upcoming' : festivalTodayKey > end ? 'ended' : 'ongoing';
    const badge = row.querySelector('[data-festival-progress]');
    badge.textContent = { upcoming: '예정', ongoing: '진행중', ended: '종료' }[progress];
    badge.className = `badge festival-progress-${progress}`;
});

// 축제 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.festival-title', 30);
shortenListText('.festival-location', 15);

setupList({
    prefix: 'festival',
    tableSelector: '.festival-table',
    idField: 'festivalId',
    exactFields: ['festivalId']
});
