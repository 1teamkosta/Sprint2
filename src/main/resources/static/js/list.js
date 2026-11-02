// 공통 목록 기능: 말줄임, 정렬, 검색, 상태 필터, 결과 건수 표시.
// 현재는 화면의 더미 데이터를 처리하며, DB 연동 시 서버 조회로 교체한다.

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

function setupTableSort(tableSelector, rows, idField) {
    const table = document.querySelector(tableSelector);
    const body = table.querySelector('tbody');
    const emptyRow = body.querySelector('tr:not([data-status])');
    const buttons = table.querySelectorAll('[data-sort]');
    const collator = new Intl.Collator('ko', { numeric: true, sensitivity: 'base' });
    let sortField = '';
    let ascending = true;

    function getValue(row, field) {
        const cell = row.querySelector(`[data-sort-field="${field}"], [data-field="${field}"]`);
        return cell.dataset.sortValue || cell.dataset.fullText || cell.textContent.trim();
    }

    buttons.forEach(function (button) {
        button.addEventListener('click', function () {
            const field = button.dataset.sort;
            ascending = sortField === field ? !ascending : true;
            sortField = field;

            rows.sort(function (first, second) {
                const result = collator.compare(getValue(first, field), getValue(second, field));
                // 같은 값은 번호순으로 유지한다.
                return result === 0
                    ? collator.compare(getValue(first, idField), getValue(second, idField))
                    : result * (ascending ? 1 : -1);
            });
            rows.forEach(function (row) {
                body.insertBefore(row, emptyRow);
            });

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
        });
    });
}

// prefix는 HTML 요소 ID의 공통 앞부분(festival, meeting, user)이다.
function setupList({ prefix, tableSelector, idField, exactFields = [], hidePaginationWhenFiltered = false }) {
    const table = document.querySelector(tableSelector);
    const rows = Array.from(table.querySelectorAll('tbody tr[data-status]'));
    const form = document.querySelector(`.${prefix}-search`);
    const statusFilter = document.querySelector(`#${prefix}-status-filter`);
    const searchType = document.querySelector(`#${prefix}-search-type`);
    const keyword = document.querySelector(`#${prefix}-keyword`);
    const resultCount = document.querySelector(`#${prefix}-result-count`);
    const emptyRow = document.querySelector(`#${prefix}-empty`);
    const pagination = document.querySelector(`#${prefix}-pagination`);
    const statusButtons = statusFilter ? statusFilter.querySelectorAll('[data-status-filter]') : [];
    let selectedStatus = 'all';
    let selectedSearchType = '';
    let searchKeyword = '';

    // 말줄임 여부와 관계없이 검색·정렬에는 원문을 사용한다.
    rows.forEach(function (row) {
        row.querySelectorAll('[data-field]').forEach(function (cell) {
            if (!cell.dataset.fullText) cell.dataset.fullText = cell.textContent.trim();
        });
    });

    function filterRows() {
        let count = 0;
        rows.forEach(function (row) {
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
            row.hidden = !(matchesStatus && matchesKeyword);
            if (!row.hidden) count += 1;
        });

        resultCount.textContent = `전체 ${rows.length}건 · 검색 결과 ${count}건`;
        emptyRow.hidden = count !== 0;
        const filtered = selectedStatus !== 'all' || searchKeyword !== '';
        pagination.hidden = count === 0 || (hidePaginationWhenFiltered && filtered);
    }

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        selectedSearchType = searchType.value;
        searchKeyword = keyword.value.trim().toLocaleLowerCase();
        filterRows();
    });

    statusButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            selectedStatus = button.dataset.statusFilter;
            statusButtons.forEach(function (option) {
                option.setAttribute('aria-pressed', String(option === button));
            });
            statusFilter.open = false;
            statusFilter.querySelector('summary').focus();
            filterRows();
        });
    });

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

    setupTableSort(tableSelector, rows, idField);
    filterRows();
}
