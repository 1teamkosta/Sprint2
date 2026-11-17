/**
 * [역할과 실행 흐름]
 * 목록의 말줄임, 검색, 상태 필터, 정렬, 건수 표시와 공통 페이지네이션을 담당한다.
 * HTML에서 list.js를 먼저 불러온 뒤 화면별 JS에서 말줄임 → setupList() 순으로 호출한다.
 * 각 화면 JS는 선택자와 정책만 설정하고, 공통 동작을 바꿀 때는 이 파일을 수정한다.
 *
 * [현재 범위 / 서버 연동]
 * 페이지를 열 때 HTML에 있는 행을 처리한다. 서버 요청이나 DB 저장은 하지 않는다.
 * setupList()가 검색·필터 결과를 paginateItems()로 나누고 renderPagination()으로 버튼을 만든다.
 * 태그 관리도 같은 페이지네이션 함수를 사용한다.
 * 서버 연동 시 검색·정렬·페이지 조건을 전달해 목록과 전체 건수를 받아오는 방식으로 교체한다.
 */
// 공통 목록 기능: 말줄임, 정렬, 검색, 상태 필터, 결과 건수 표시, 페이지네이션.
// 현재는 화면의 더미 데이터를 처리하며, DB 연동 시 서버 조회로 교체한다.

/**
 * 표시 글자를 limit까지 줄인다. 공백도 길이에 포함한다.
 * data-full-text에 원문을 보관하므로 줄여진 부분도 검색·정렬에 사용할 수 있다.
 * 셀 안에 a 태그가 있으면 링크 글자만 줄여서 상세 링크가 사라지지 않게 한다.
 * 예: shortenListText('.festival-title', 30);
 */
function shortenListText(selector, limit) {
    document.querySelectorAll(selector).forEach(function (cell) {
        // 셀 전체를 덮어쓰면 a 태그가 사라지므로 링크 내부의 글자만 줄인다.
        const textTarget = cell.querySelector('a') || cell;
        const fullText = cell.dataset.fullText || textTarget.textContent.trim();
        cell.dataset.fullText = fullText;
        const characters = Array.from(fullText);
        if (characters.length > limit) {
            textTarget.textContent = characters.slice(0, limit).join('') + '...';
            textTarget.title = fullText;
            textTarget.setAttribute('aria-label', fullText);
        }
    });
}

/**
 * 배열을 페이지당 pageSize개씩 나누고 현재 페이지의 항목·페이지 정보를 반환한다.
 * 검색·필터·정렬을 먼저 적용한 배열을 전달해야 전체 결과의 순서가 유지된다.
 * 삭제나 검색으로 결과가 줄어들면 존재하는 페이지 범위로 자동 보정한다.
 * 예: const page = paginateItems(results, currentPage, 10);
 *     page.items로 행을 만들고 page.currentPage를 화면의 현재 페이지에 반영한다.
 * 서버가 이미 페이지를 나눠 응답하는 경우에는 이 함수를 다시 적용하지 않는다.
 */
function paginateItems(items, currentPage = 1, pageSize = 10) {
    if (!Number.isInteger(pageSize) || pageSize < 1) {
        throw new RangeError('페이지당 개수는 1 이상의 정수여야 합니다.');
    }
    const totalCount = items.length;
    const pageCount = Math.max(1, Math.ceil(totalCount / pageSize));
    const page = Math.min(Math.max(1, Math.trunc(Number(currentPage)) || 1), pageCount);
    const start = (page - 1) * pageSize;
    return {
        items: items.slice(start, start + pageSize),
        currentPage: page,
        pageCount,
        totalCount
    };
}

/**
 * 공통 Bootstrap 페이지 버튼(이전·번호·다음)을 만든다.
 * containerSelector는 nav, pagesSelector는 그 안의 ul 선택자다.
 * 나머지 페이지 정보는 paginateItems() 결과에서 받아 전달한다.
 * 클릭 시 onPageChange(선택한 페이지)를 호출하므로, 각 화면은 현재 페이지를 바꾸고 다시 그린다.
 * 결과 0건은 nav를 숨기고 첫/마지막 페이지의 이동 버튼은 비활성화한다.
 */
function renderPagination({ containerSelector, pagesSelector, currentPage, pageCount, totalCount, onPageChange }) {
    const container = document.querySelector(containerSelector);
    const pages = document.querySelector(pagesSelector);
    pages.replaceChildren();
    container.hidden = totalCount === 0;

    function addPageButton(label, targetPage, disabled, active) {
        const item = document.createElement('li');
        item.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;
        const button = document.createElement('button');
        button.className = 'page-link';
        button.type = 'button';
        button.textContent = label;
        button.disabled = disabled;
        button.setAttribute('aria-label', typeof label === 'number' ? `${label}페이지` : `${label} 페이지`);
        if (active) button.setAttribute('aria-current', 'page');
        button.addEventListener('click', function () {
            onPageChange(targetPage);
            // 다시 그려진 현재 페이지 버튼으로 키보드 포커스를 복원한다.
            const currentButton = pages.querySelector('button[aria-current="page"]');
            if (currentButton) currentButton.focus();
        });
        item.append(button);
        pages.append(item);
    }

    addPageButton('이전', currentPage - 1, currentPage === 1, false);
    for (let page = 1; page <= pageCount; page += 1) {
        addPageButton(page, page, false, page === currentPage);
    }
    addPageButton('다음', currentPage + 1, currentPage === pageCount, false);
}

/**
 * setupList()가 호출하는 정렬 연결 함수.
 * 버튼의 data-sort와 셀의 data-field 또는 data-sort-field를 같은 이름으로 맞춘다.
 * 날짜처럼 표시 형식이 다르면 data-sort-value="2026-11-02"에 비교용 값을 넣는다.
 * 번호는 idField로 받아, 같은 값인 행들의 순서를 번호순으로 유지한다.
 */
function setupTableSort(tableSelector, rows, idField, onSort = function () {}) {
    const table = document.querySelector(tableSelector);
    const body = table.querySelector('tbody');
    const emptyRow = body.querySelector('tr:not([data-status])');
    const buttons = table.querySelectorAll('[data-sort]');
    // 한국어 문자열을 비교하고, 숫자도 1 → 2 → 10 순으로 정렬한다.
    const collator = new Intl.Collator('ko', { numeric: true, sensitivity: 'base' });
    let sortField = '';
    let ascending = true;

    // 비교 전용 값 → 말줄임 전 원문 → 화면 글자 순서로 비교 값을 선택한다.
    function getValue(row, field) {
        const cell = row.querySelector(`[data-sort-field="${field}"], [data-field="${field}"]`);
        return cell.dataset.sortValue || cell.dataset.fullText || cell.textContent.trim();
    }

    buttons.forEach(function (button) {
        button.addEventListener('click', function () {
            const field = button.dataset.sort;
            // 새 컬럼은 오름차순부터, 같은 컬럼을 다시 누르면 방향을 반전한다.
            ascending = sortField === field ? !ascending : true;
            sortField = field;

            rows.sort(function (first, second) {
                const result = collator.compare(getValue(first, field), getValue(second, field));
                // 같은 값은 번호순으로 유지한다.
                return result === 0
                    ? collator.compare(getValue(first, idField), getValue(second, idField))
                    : result * (ascending ? 1 : -1);
            });
            // 기존 행을 이동하므로 링크·버튼 이벤트와 필터의 hidden 상태가 유지된다.
            // 결과 없음 안내 행은 tbody 마지막에 남긴다.
            rows.forEach(function (row) {
                body.insertBefore(row, emptyRow);
            });

            // 선택된 컬럼의 화살표와 보조기기용 aria-sort를 함께 갱신한다.
            // 정렬 버튼에는 화살표를 담을 span, 버튼 바깥에는 th가 있어야 한다.
            buttons.forEach(function (item) {
                const active = item === button;
                const heading = item.closest('th');
                if (active) {
                    heading.setAttribute('aria-sort', ascending ? 'ascending' : 'descending');
                } else {
                    heading.removeAttribute('aria-sort');
                }
                item.querySelector('span').textContent = active ? (ascending ? '↑' : '↓') : '↕';
            });
            // 전체 결과를 정렬한 뒤 현재 페이지의 표시 행을 다시 계산한다.
            onSort();
        });
    });
}

// prefix는 HTML 요소 ID의 공통 앞부분(festival, club, user)이다.
/**
 * [화면별 설정]
 * prefix: HTML ID·클래스의 앞부분. user라면 .user-search, #user-keyword 등과 연결된다.
 * tableSelector: 테이블 선택자. 예: '.user-table'.
 * idField: 같은 값인 행의 순서를 결정할 고유 번호 필드. 예: 'userId'.
 * exactFields: 부분 일치 대신 정확히 일치시킬 검색 필드. 예: ['userId'].
 * defaultSearchType: 검색 유형 선택창이 없을 때 사용할 고정 검색 필드. 예: 'title'.
 * pageSize: 한 페이지 표시 개수. 기본값은 10개다.
 * extraFilter: 화면별 추가 필터 함수. 행을 받아 표시 대상이면 true를 반환한다.
 * 반환 객체의 refresh()는 검색·정렬을 유지한 채 추가 필터를 다시 적용하고 1페이지로 이동한다.
 *
 * [HTML 연결 규칙]
 * - 데이터 행: data-status. 상태 필터가 없는 회원 화면도 행 구분용으로 붙인다.
 * - 검색 셀: data-field. 검색 select의 option value와 같은 이름을 사용한다.
 * - 필수 요소: .접두사-search, #접두사-keyword / -result-count / -empty / -pagination.
 * - 검색 유형 선택창: #접두사-search-type. 없으면 defaultSearchType을 사용한다.
 * - 결과 없음 행은 data-status 없이 tbody 마지막에 둔다.
 * - 상태 필터는 선택 사항. #접두사-status-filter 내부의 data-status-filter 버튼을 사용한다.
 *
 * 초기화 시 수집한 행을 계속 사용하므로 HTML 준비 후 한 번만 호출한다.
 * 이후 행을 새로 만드는 동적 목록은 별도 처리가 필요하다. tags.js가 그 예다.
 */
function setupList({ prefix, tableSelector, idField, exactFields = [], defaultSearchType = '', pageSize = 10, extraFilter = function () { return true; } }) {
    const table = document.querySelector(tableSelector);
    const rows = Array.from(table.querySelectorAll('tbody tr[data-status]'));
    const form = document.querySelector(`.${prefix}-search`);
    const statusFilter = document.querySelector(`#${prefix}-status-filter`);
    const searchType = document.querySelector(`#${prefix}-search-type`);
    const keyword = document.querySelector(`#${prefix}-keyword`);
    const resultCount = document.querySelector(`#${prefix}-result-count`);
    const emptyRow = document.querySelector(`#${prefix}-empty`);
    const statusButtons = statusFilter ? statusFilter.querySelectorAll('[data-status-filter]') : [];
    // 각 setupList() 호출 내부에서 상태를 관리하므로 화면별 검색 조건이 섞이지 않는다.
    let selectedStatus = 'all';
    let selectedSearchType = defaultSearchType;
    let searchKeyword = '';
    let currentPage = 1;

    // 말줄임 여부와 관계없이 검색·정렬에는 원문을 사용한다.
    rows.forEach(function (row) {
        row.querySelectorAll('[data-field]').forEach(function (cell) {
            if (!cell.dataset.fullText) cell.dataset.fullText = cell.textContent.trim();
        });
    });

    // 상태·검색 조건에 맞는 전체 결과를 구한 다음 현재 페이지의 행만 표시한다.
    // 검색 대상 미선택 시 data-field가 있는 모든 셀 중 하나라도 일치하면 표시한다.
    function filterRows() {
        const results = rows.filter(function (row) {
            const matchesStatus = selectedStatus === 'all' || row.dataset.status === selectedStatus;
            const fields = selectedSearchType
                ? row.querySelectorAll(`[data-field="${selectedSearchType}"]`)
                : row.querySelectorAll('[data-field]');
            const matchesKeyword = Array.from(fields).some(function (cell) {
                const value = cell.dataset.fullText.toLocaleLowerCase();
                return exactFields.includes(selectedSearchType) && searchKeyword !== ''
                    ? value === searchKeyword
                    : value.includes(searchKeyword);
            });
            return matchesStatus && matchesKeyword && extraFilter(row);
        });
        const pageData = paginateItems(results, currentPage, pageSize);
        currentPage = pageData.currentPage;
        const visibleRows = new Set(pageData.items);
        rows.forEach(function (row) {
            row.hidden = !visibleRows.has(row);
        });

        // '전체'는 현재 HTML의 행 수다. DB 전체 건수는 나중에 서버 응답으로 받아야 한다.
        resultCount.textContent = `전체 ${rows.length}건 · 검색 결과 ${results.length}건`;
        emptyRow.hidden = results.length !== 0;
        renderPagination({
            containerSelector: `#${prefix}-pagination`,
            pagesSelector: `#${prefix}-pagination .pagination`,
            currentPage,
            pageCount: pageData.pageCount,
            totalCount: pageData.totalCount,
            onPageChange: function (page) {
                currentPage = page;
                filterRows();
            }
        });
    }

    // 검색 버튼과 Enter를 submit 하나로 처리한다. 입력 중이 아니라 제출한 시점의 값을 보관한다.
    // trim()은 앞뒤 공백 제거, 소문자 변환은 영문 대소문자 구분 없는 검색을 위한 처리다.
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        selectedSearchType = searchType ? searchType.value : defaultSearchType;
        searchKeyword = keyword.value.trim().toLocaleLowerCase();
        currentPage = 1;
        filterRows();
    });

    // 상태를 바꿀 때 기존 검색 조건은 유지한다. 선택 후 닫고 컬럼명으로 포커스를 돌려준다.
    statusButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            selectedStatus = button.dataset.statusFilter;
            currentPage = 1;
            statusButtons.forEach(function (option) {
                option.setAttribute('aria-pressed', String(option === button));
            });
            statusFilter.open = false;
            statusFilter.querySelector('summary').focus();
            filterRows();
        });
    });

    // 상태 필터가 있는 화면에만 바깥 클릭·Escape 닫기를 연결한다.
    if (statusFilter) {
        document.addEventListener('click', function (event) {
            if (!statusFilter.contains(event.target)) statusFilter.open = false;
        });

        statusFilter.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                statusFilter.open = false;
                statusFilter.querySelector('summary').focus();
            }
        });
    }

    // 최초 표시: 원문 보관과 이벤트 연결을 마친 뒤 정렬을 연결하고 초기 건수를 갱신한다.
    setupTableSort(tableSelector, rows, idField, function () {
        currentPage = 1;
        filterRows();
    });
    filterRows();
    return {
        refresh: function () {
            currentPage = 1;
            filterRows();
        }
    };
}
