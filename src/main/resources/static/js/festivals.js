// 로컬 날짜를 YYYY-MM-DD로 비교한다. 시작일과 종료일 당일은 모두 진행중이다.
const festivalToday = new Date();
const festivalTodayKey = `${festivalToday.getFullYear()}-${String(festivalToday.getMonth() + 1).padStart(2, '0')}-${String(festivalToday.getDate()).padStart(2, '0')}`;
document.querySelectorAll('.festival-table tbody tr[data-status]').forEach(function (row) {
    const dates = row.querySelectorAll('[data-sort-field="startDate"] time');
    const start = dates[0].getAttribute('datetime');
    const end = dates[1].getAttribute('datetime');
    const progress = festivalTodayKey < start ? 'upcoming' : festivalTodayKey > end ? 'ended' : 'ongoing';
    row.dataset.progress = progress;
    const badge = row.querySelector('[data-festival-progress]');
    badge.textContent = { upcoming: '예정', ongoing: '진행중', ended: '종료' }[progress];
    badge.className = `badge festival-progress-${progress}`;
});

// 태그 관리의 음악·먹거리·전통문화 색상에 맞춘 시연용 장르 배지.
// 서버 연동 시 축제의 장르 태그 color 값을 그대로 사용한다.
const festivalGenreColors = { '음악': '#FADADD', '먹거리': '#FFE0B2', '문화': '#FFF0AD' };
document.querySelectorAll('.festival-genre-tag').forEach(function (badge) {
    const color = festivalGenreColors[badge.textContent.trim()];
    if (!color) return;
    badge.style.backgroundColor = color;
    badge.style.color = '#' + [1, 3, 5].map(function (offset) {
        return Math.round(parseInt(color.slice(offset, offset + 2), 16) * 0.3)
            .toString(16).padStart(2, '0');
    }).join('').toUpperCase();
});

// 축제 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.festival-title', 30);
shortenListText('.festival-location', 15);

let selectedFestivalProgress = 'all';
const festivalList = setupList({
    prefix: 'festival',
    tableSelector: '.festival-table',
    idField: 'festivalId',
    exactFields: ['festivalId'],
    extraFilter: function (row) {
        return selectedFestivalProgress === 'all' || row.dataset.progress === selectedFestivalProgress;
    }
});

// 공개 상태·검색·정렬을 유지한 채 진행상황을 바꾸고 1페이지로 돌아간다.
const festivalProgressFilter = document.querySelector('#festival-progress-filter');
const festivalProgressButtons = festivalProgressFilter.querySelectorAll('[data-progress-filter]');
festivalProgressButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        selectedFestivalProgress = button.dataset.progressFilter;
        festivalProgressButtons.forEach(function (item) {
            item.setAttribute('aria-pressed', String(item === button));
        });
        festivalList.refresh();
        festivalProgressFilter.open = false;
        festivalProgressFilter.querySelector('summary').focus();
    });
});
document.addEventListener('click', function (event) {
    if (!festivalProgressFilter.contains(event.target)) festivalProgressFilter.open = false;
});
festivalProgressFilter.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        festivalProgressFilter.open = false;
        festivalProgressFilter.querySelector('summary').focus();
    }
});
