// 공통 목록 기능: 말줄임, 정렬, 검색, 상태 필터, 결과 건수 표시, 페이지네이션.

//말줄임 함수
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

//페이지네이션 함수
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

//정렬버튼을 누르면 서버에서 정렬을 요청하고, 성공한 뒤 화살표를 바꾸는 함수
function setupTableSort(tableSelector, onSort) {
    const table = document.querySelector(tableSelector);
    const buttons = table.querySelectorAll('[data-sort]');

    let sortType = '';
    let sortDirection = 'DESC';
    let loading = false;

    buttons.forEach(function (button) {
        button.addEventListener('click', async function () {
            if (loading) return;

            const nextType = button.dataset.sort;
            const nextDirection =
                sortType === nextType && sortDirection === 'ASC'
                    ? 'DESC'
                    : 'ASC';

            loading = true;

            try {
                // 서버 조회가 성공할 때까지 기다린다.
                await onSort(nextType, nextDirection);

                sortType = nextType;
                sortDirection = nextDirection;

                buttons.forEach(function (item) {
                    const active = item === button;
                    const heading = item.closest('th');

                    if (active) {
                        heading.setAttribute(
                            'aria-sort',
                            sortDirection === 'ASC'
                                ? 'ascending'
                                : 'descending'
                        );
                    } else {
                        heading.removeAttribute('aria-sort');
                    }

                    item.querySelector('span').textContent =
                        active
                            ? (sortDirection === 'ASC' ? '↑' : '↓')
                            : '↕';
                });
            } catch (error) {
                console.error('정렬 조회에 실패했습니다.', error);
            } finally {
                loading = false;
            }
        });
    });
}

// 목록에서 전체 선택, 상태 필터, 정렬, 검색, 페이지 버튼을 연결하는 초기 설정 함수
function setupList({ prefix,
                       tableSelector,
                       onQueryChange
}) {
    const table = document.querySelector(tableSelector);
    const statusFilter = document.querySelector(`#${prefix}-status-filter`);
    let currentPage = 1;
    const selectAll = document.querySelector(`#${prefix}-select-all`);


    function getVisibleCheckboxes() {
        return Array.from(
            table.querySelectorAll('tbody .list-selection-input')
        );
    }

// 전체 선택 체크박스의 체크/부분 선택 상태를 현재 화면에 맞춘다.
    function SelectAll() {
        if (!selectAll) return;
        const boxes = getVisibleCheckboxes();
        const checkedCount = boxes.filter(function (box) { return box.checked; }).length;
        selectAll.checked = boxes.length > 0 && checkedCount === boxes.length;
    }

    if (selectAll) {
        // 전체 선택을 누르면 보이는 행만 같은 상태로 맞춘다.
        selectAll.addEventListener('change', function () {
            getVisibleCheckboxes().forEach(function (box) { box.checked = selectAll.checked; });
        });
        // 행 체크박스를 하나씩 누를 때 전체 선택 상태를 동기화한다.
        table.addEventListener('change', function (event) {
            if (event.target.matches('.list-selection-input')) SelectAll();
        });
    }



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

        //상태 필터클릭 이벤트
        const statusButtons = statusFilter.querySelectorAll('[data-status-filter]');

        statusButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                const selected = button.dataset.statusFilter;

                const filterByStatus = selected === 'all'
                    ? null
                    : selected === 'visible' ? 1 : 0;

                statusFilter.open = false;
                return onQueryChange({
                    page: 1,
                    filterByStatus
                });

            });
        });
    }

    // 최초 표시: 원문 보관과 이벤트 연결을 마친 뒤 정렬을 연결하고 초기 건수를 갱신한다.
    setupTableSort(tableSelector, function (sortType, sortDirection) {
        currentPage = 1;

        return onQueryChange({
            page: currentPage,
            sortType,
            sortDirection
        });
    });

    //검색 이벤트
    const form = document.querySelector(`.${prefix}-search`);
    const searchType = document.querySelector(`#${prefix}-search-type`);
    const keyword = document.querySelector(`#${prefix}-keyword`);

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // form의 기본 페이지 이동 방지

        return onQueryChange({
            page: 1,
            searchBy: searchType.value,
            keyword: keyword.value
        });
    });

    const pagination = document.querySelector(`#${prefix}-pagination`);

    renderPagination({
        containerSelector: `#${prefix}-pagination`,
        pagesSelector: `#${prefix}-pagination .pagination`,
        currentPage: Number(pagination.dataset.currentPage),
        pageCount: Number(pagination.dataset.totalPages),
        totalCount: Number(pagination.dataset.totalCount),
        onPageChange: function (page) {
            onQueryChange({ page });
        }
    });

    return {
        refresh: function () {
            return onQueryChange({ page: 1 });
        },

        resetSelection: function () {
            getVisibleCheckboxes().forEach(function (box) {
                box.checked = false;
            });

            if (selectAll) {
                selectAll.checked = false;
                selectAll.indeterminate = false;
            }
        }
    };
}
