// 모임 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.club-title', 30);
shortenListText('.club-festival', 30);
shortenListText('.club-table [data-field="host"]', 5);

let selectedClubType = 'all';
const clubList = setupList({
    prefix: 'club',
    tableSelector: '.club-table',
    idField: 'clubId',
    extraFilter: function (row) {
        return selectedClubType === 'all' || row.dataset.clubType === selectedClubType;
    }
});

// 탭은 모임 화면에서 관리하고, 공통 목록에 추가 조건만 전달한다.
// 검색·공개 상태·정렬 기준은 유지하며 탭을 바꾸면 1페이지부터 보여준다.
const clubTabs = Array.from(document.querySelectorAll('[data-club-type-tab]'));
function selectClubType(type) {
    selectedClubType = type;
    clubTabs.forEach(function (tab) {
        const active = tab.dataset.clubTypeTab === type;
        tab.classList.toggle('active', active);
        tab.setAttribute('aria-selected', String(active));
        tab.tabIndex = active ? 0 : -1;
        if (active) document.querySelector('#club-tab-panel').setAttribute('aria-labelledby', tab.id);
    });
    clubList.refresh();
}

clubTabs.forEach(function (tab, index) {
    tab.addEventListener('click', function () { selectClubType(tab.dataset.clubTypeTab); });
    tab.addEventListener('keydown', function (event) {
        if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return;
        event.preventDefault();
        const next = event.key === 'Home' ? 0 : event.key === 'End' ? clubTabs.length - 1
            : (index + (event.key === 'ArrowRight' ? 1 : -1) + clubTabs.length) % clubTabs.length;
        clubTabs[next].focus();
        selectClubType(clubTabs[next].dataset.clubTypeTab);
    });
});
