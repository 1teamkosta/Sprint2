// 축제 관리 화면의 말줄임과 공통 목록 설정
shortenListText('.festival-title', 30);
shortenListText('.festival-location', 15);

setupList({
    prefix: 'festival',
    tableSelector: '.festival-table',
    idField: 'festivalId',
    exactFields: ['festivalId'],
    // 3페이지 표시는 UI 예시이므로 검색·필터 적용 시 숨김.
    hidePaginationWhenFiltered: true
});
