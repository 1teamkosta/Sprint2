const tagPalette = [
    '#FADADD', '#FBE5D6', '#FFE0B2', '#FFF0AD', '#F1F4B8',
    '#DDECCB', '#CDEBDD', '#CCEFEB', '#D3EFF5', '#DCEAF5',
    '#D8DEFA', '#E2D9F3', '#ECDDF4', '#F2D9EB', '#F5DEDE',
    '#E6DDD2', '#E2E7ED'
];
const tagPaletteNames = [
    '연분홍', '살구', '연주황', '연노랑', '연레몬',
    '연두', '민트', '연청록', '하늘', '연파랑',
    '연남색', '라벤더', '연보라', '연자주', '로즈',
    '베이지', '회청색'
];
const tagTypeNames = { all: '전체', genre: '축제 장르', target: '추천 대상' };
const tagTypeCodes = { genre: 0, target: 1 };
const tagDialog = document.querySelector('#tag-dialog');
const tagForm = document.querySelector('#tag-form');
const tagNameInput = document.querySelector('#tag-name');
const tagTypeInput = document.querySelector('#tag-type');
const tagStatusInput = document.querySelector('#tag-status');
const tagColorInput = document.querySelector('#tag-color');
const tagColorPalette = document.querySelector('#tag-color-palette');
const tagDescription = document.querySelector('#tag-description');
const tagTabs = document.querySelectorAll('[data-tag-type]');
const tagSortButtons = document.querySelectorAll('[data-tag-sort]');
const tagStatusFilter = document.querySelector('#tag-status-filter');
const tagSaveButton = document.querySelector('#tag-save');
let editingTagId = null;
let tagParams = new URLSearchParams(window.location.search);

//태그 추가시 html 만들어주는 함수
function tagElement(tagName, className, text) {
    const element = document.createElement(tagName);
    element.className = className;
    if (text !== undefined) element.textContent = text;
    return element;
}

function showTagError(error) {
    document.querySelector('#tag-feedback').textContent = error.message;
}

// 탭 전환시 상태, 정렬 유지
function changeTagQuery(changes) {
    const params = new URLSearchParams(tagParams);
    if (typeof changes.keyword === 'string') {
        changes = { ...changes, keyword: changes.keyword.trim() };
        if (!changes.keyword) changes = { ...changes, searchBy: null, keyword: null };
    }
    Object.entries(changes).forEach(function ([name, value]) {
        if (value == null) params.delete(name);
        else params.set(name, value);
    });
    return loadTags(params).catch(showTagError);
}

// 목록 화면 갱신
async function loadTags(params) {
    const response = await fetch('/admin/tags?' + params.toString());
    if (!response.ok) throw new Error('태그 목록 조회에 실패했습니다.');

    const result = await response.json();

    tagParams = params;
    renderTags(result.list);
    syncTagQuery();

    document.querySelector('#tag-result-count').textContent =
        '검색 결과 ' + result.total + '건';

    renderPagination({
        containerSelector: '#tag-pagination',
        pagesSelector: '#tag-pages',
        currentPage: result.pageNum,
        pageCount: result.pages,
        totalCount: result.total,
        onPageChange: function (page) {
            changeTagQuery({ page });
        }
    });
}

// 탭, 검색창, 필터, 정렬시 css 변경 함수
function syncTagQuery() {
    const type = Object.keys(tagTypeCodes).find(function (key) {
        return String(tagTypeCodes[key]) === tagParams.get('filterByTagType');
    }) || 'all';
    tagTabs.forEach(function (tab) {
        const active = tab.dataset.tagType === type;
        tab.classList.toggle('active', active);
        tab.setAttribute('aria-selected', String(active));
        if (active) document.querySelector('#tag-panel').setAttribute('aria-labelledby', tab.id);
    });
    document.querySelector('#tag-list-heading').textContent = tagTypeNames[type] + ' 태그 목록';
    document.querySelector('#tag-search').value = tagParams.get('keyword') || '';
    const status = tagParams.get('filterByStatus');
    tagStatusFilter.querySelectorAll('[data-tag-status-filter]').forEach(function (button) {
        const value = button.dataset.tagStatusFilter;
        button.setAttribute('aria-pressed', String(value === (status === '1' ? 'visible' : status === '0' ? 'hidden' : 'all')));
    });
    tagSortButtons.forEach(function (button) {
        const active = tagParams.get('sortType') === 'festivalCount';
        const ascending = tagParams.get('sortDirection') === 'ASC';
        button.closest('th').setAttribute('aria-sort', active ? (ascending ? 'ascending' : 'descending') : 'none');
        button.querySelector('span').textContent = active ? (ascending ? '↑' : '↓') : '↕';
    });
}

// 조건 적용 후 목록을 만드는 함수
function renderTags(tags) {
    const bodies = document.querySelectorAll('.tag-table tbody');
    const body = bodies[0] || document.createElement('tbody');
    body.id = 'tag-list';
    body.className = 'text-center';
    if (bodies.length === 0) document.querySelector('.tag-table').append(body);
    bodies.forEach(function (item, index) {
        if (index > 0) item.remove();
    });
    const fragment = document.createDocumentFragment();
    tags.forEach(function (tag) {
        const row = document.createElement('tr');
        const isPublic = tag.status === 1;
        const statusCell = document.createElement('td');
        statusCell.append(tagElement('span', 'badge tag-status-' + (isPublic ? 'visible' : 'hidden'), isPublic ? '공개' : '비공개'));
        const nameCell = document.createElement('td');
        const badge = tagElement('span', 'tag-name-badge', tag.tagName);
        const color = /^#[0-9a-f]{6}$/i.test(tag.color) ? tag.color : tagPalette[0];
        badge.style.backgroundColor = color;
        badge.title = tag.tagName;
        nameCell.append(badge);
        row.append(tagElement('td', '', tag.tagNumber), statusCell, nameCell,
            tagElement('td', 'tag-description text-start', tag.contents || '—'),
            tagElement('td', '', tag.festivalCount));
        const actions = document.createElement('td');
        const group = tagElement('div', 'tag-actions');
        group.append(createTagEditButton(tag.tagNumber));
        actions.append(group);
        row.append(actions);
        fragment.append(row);
    });
    if (tags.length === 0) {
        const row = document.createElement('tr');
        const cell = tagElement('td', 'py-5 text-secondary', '조건에 맞는 태그가 없습니다.');
        cell.colSpan = 6;
        row.append(cell);
        fragment.append(row);
    }
    body.replaceChildren(fragment);
}

//수정 버튼 클릭시 모달 열기
function createTagEditButton(tagNumber) {
    const button = tagElement('button', 'btn btn-outline-secondary btn-sm', '수정');
    button.type = 'button';
    button.addEventListener('click', function () {
        openTagDialog(tagNumber).catch(showTagError);
    });
    return button;
}

// 모달 태그 색상 선택 체크 표시하는 함수
function renderTagPalette() {
    tagColorPalette.replaceChildren();
    const colors = tagPalette;
    colors.forEach(function (color, index) {
        const selected = color === tagColorInput.value;
        const name = tagPaletteNames[index];
        const button = tagElement('button', 'tag-color-option', selected ? '✓' : '');
        button.type = 'button';
        button.style.backgroundColor = color;
        button.title = name;
        button.setAttribute('aria-label', name);
        button.setAttribute('aria-pressed', String(selected));
        button.addEventListener('click', function () {
            tagColorInput.value = color;
            renderTagPalette();
        });
        tagColorPalette.append(button);
        if (selected) document.querySelector('#tag-color-selection').textContent = `${name} 선택됨`;
    });
}

//추가, 수정 모달 함수
async function openTagDialog(tagNumber) {
    let tag = null;
    if (tagNumber != null) {
        const response = await fetch('/admin/tag/' + tagNumber);
        const detail = await response.text();
        tag = detail ? JSON.parse(detail) : null;
    }
    editingTagId = tag ? tag.tagNumber : null;
    tagForm.reset();
    tagNameInput.setCustomValidity('');
    tagNameInput.value = tag ? tag.tagName : '';
    const currentType = tagParams.get('filterByTagType');
    tagTypeInput.value = tag ? (tag.tagType === tagTypeCodes.target ? 'target' : 'genre')
        : currentType === String(tagTypeCodes.target) ? 'target' : 'genre';
    tagColorInput.value = tag && /^#[0-9a-f]{6}$/i.test(tag.color) ? tag.color.toUpperCase() : tagPalette[0];
    tagDescription.value = tag ? tag.contents || '' : '';
    tagStatusInput.value = tag && tag.status === 0 ? 'hidden' : 'visible';
    document.querySelector('#tag-dialog-title').textContent = tag ? '태그 수정' : '태그 추가';
    tagSaveButton.textContent = tag ? '저장' : '태그 추가';
    document.querySelector('#tag-dialog-close').setAttribute('aria-label', tag ? '태그 수정 창 닫기' : '태그 추가 창 닫기');
    document.querySelector('#tag-feedback').textContent = '';
    renderTagPalette();
    tagDialog.showModal();
}

function closeTagDialog() {
    tagDialog.close();
}

//탭 선택
tagTabs.forEach(function (tab) {
    tab.addEventListener('click', function () {
        changeTagQuery({ page: 1, filterByTagType: tagTypeCodes[tab.dataset.tagType] ?? null });
    });
});

//정렬 선택
tagSortButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        changeTagQuery({
            page: 1,
            sortType: 'festivalCount',
            sortDirection: tagParams.get('sortType') === 'festivalCount' && tagParams.get('sortDirection') === 'ASC' ? 'DESC' : 'ASC'
        });
    });
});

//검색
document.querySelector('#tag-search-form').addEventListener('submit', function (event) {
    event.preventDefault();
    changeTagQuery({ page: 1, searchBy: 'tagName', keyword: document.querySelector('#tag-search').value });
});

//상태필터
tagStatusFilter.querySelectorAll('[data-tag-status-filter]').forEach(function (button) {
    button.addEventListener('click', function () {
        const status = button.dataset.tagStatusFilter;
        changeTagQuery({ page: 1, filterByStatus: status === 'all' ? null : status === 'visible' ? 1 : 0 });
        tagStatusFilter.open = false;
    });
});

//상태필터 모달 닫기
document.addEventListener('click', function (event) {
    if (!tagStatusFilter.contains(event.target)) tagStatusFilter.open = false;
});

document.querySelector('#tag-add-button').addEventListener('click',function () {
    openTagDialog().catch(showTagError);
});
document.querySelector('#tag-dialog-close').addEventListener('click', closeTagDialog);
document.querySelector('#tag-dialog-cancel').addEventListener('click', closeTagDialog);
tagNameInput.addEventListener('input', function () { tagNameInput.setCustomValidity(''); });

//태그추가, 태그 수정
tagForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    const name = tagNameInput.value.trim();
    const color = tagColorInput.value;
    tagNameInput.setCustomValidity(name ? '' : '태그명을 입력해 주세요.');
    if (!tagForm.reportValidity()) return;
    if (!/^#[0-9a-f]{6}$/i.test(color)) {
        document.querySelector('#tag-color-selection').textContent = '팔레트에서 색상을 선택해 주세요.';
        return;
    }
    const editing = editingTagId != null;
    const payload = {
        tagName: name,
        tagType: tagTypeCodes[tagTypeInput.value],
        status: tagStatusInput.value === 'visible' ? 1 : 0,
        color,
        contents: tagDescription.value.trim()
    };
    if (editing) payload.tagNumber = editingTagId;
    try {
        const response = await fetch('/admin/tag', {
            method: editing ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const message = response.status === 409 ? '같은 종류에 이미 등록된 태그명입니다.' : '태그 저장에 실패했습니다.';
            alert(message);
            return;
        }
        location.reload();
    } catch (error) {
        if (tagDialog.open) {
            alert('태그 저장 요청에 실패했습니다. 연결 상태를 확인해 주세요.');
        }
        showTagError(error);
    }
});

// 수정 > 버튼으로 변경
document.querySelectorAll('.tag-table tbody tr').forEach(function (row) {
    const cells = row.children;
    if (cells.length !== 6) return;
    const tagNumber = Number(cells[0].textContent);
    cells[5].replaceChildren(createTagEditButton(tagNumber));
});

//페이지네이션을 위한 페이지 정보 저장
const pagination = document.querySelector('#tag-pagination');
renderPagination({
    containerSelector: '#tag-pagination',
    pagesSelector: '#tag-pages',
    currentPage: Number(pagination.dataset.currentPage),
    pageCount: Number(pagination.dataset.totalPages),
    totalCount: Number(pagination.dataset.totalCount),
    onPageChange: function (page) {
        changeTagQuery({ page });
    }
});
