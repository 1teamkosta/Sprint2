// 제목 검색·작성일 정렬·10개씩 페이지 이동은 공통 목록 기능을 재사용한다.
// 검색 유형 선택 없이 항상 제목 필드만 검색한다.
setupList({
    prefix: 'notice',
    tableSelector: '.notice-table',
    idField: 'noticeId',
    defaultSearchType: 'title',
    pageSize: 10
});
