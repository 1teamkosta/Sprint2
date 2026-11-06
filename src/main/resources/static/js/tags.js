/**
 * 태그 종류 탭, 태그명 검색, 페이지 이동, 추가·수정 모달을 관리하는 화면 전용 코드
 * 데이터 준비 → 이벤트 연결 → renderTags()로 최초 화면 표시 순으로 실행
 * 사용자가 동작하면 상태나 tags 배열을 바꾸고 renderTags()로 화면을 다시 그림
 *
 * 기존 목록 화면은 HTML 행을 숨기지만, 이 파일은 배열에서 행을 새로 만든다.
 * 페이지 분리와 버튼 생성은 list.js의 paginateItems() / renderPagination()을 재사용한다.
 * 태그처럼 추가·수정·공개 상태 변경하는 화면을 만들 때 참고할 수 있지만, 그대로 쓰는 범용 모듈은 아니다.
 * 다른 화면에 적용한다면 HTML ID와 데이터 항목을 함께 맞춰야 한다.
 *
 * [현재 범위 / 서버 연동]
 * 현재 변경은 메모리에만 남는다. 파일·브라우저 저장소·DB에 저장하지 않고 새로고침 시 초기화된다.
 * 서버 연동 시 조회 결과로 tags를 채우고, 변경 API 성공 후 목록·집계를 다시 조회한다.
 */
// 태그 관리 시연용 데이터: 새로고침하면 초기 데이터로 돌아온다.
// 태그 색상은 공통 테마 변수가 아닌 개별 데이터로 취급한다.
// DB연결 후 삭제 예정
// 더미 태그와 추가 모달의 기본 색상이다. 선택한 색상은 :root 변수로 등록하지 않는다.
// 나중에 DB에서 color: '#FFF0AD' 같은 HEX 문자열을 받아도 아래 표시 코드를 사용할 수 있다.
// 파스텔 팔레트: 실제 태그 색상 데이터이며 CSS 변수나 저장소에 등록하지 않는다.
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
// 더미 원본: [태그명, 종류, 설명, 사용 중인 축제 수].
// 아래 map()에서 { id, name, type, description, usage, color } 객체로 변환한다.
// 서버 응답도 이 필드 구조에 맞추면 화면 생성 코드를 유지하기 쉽다.
let tags = [
    ['음악', 'genre', '음악과 공연 중심의 축제', 42],
    ['도시', 'genre', '도시의 문화와 일상을 즐기는 축제', 18],
    ['먹거리', 'genre', '다양한 음식을 맛보는 축제', 35],
    ['전통문화', 'genre', '지역의 전통과 문화를 체험하는 축제', 24],
    ['예술', 'genre', '전시와 예술 활동을 즐기는 축제', 16],
    ['자연', 'genre', '자연 속에서 즐기는 축제', 21],
    ['꽃', 'genre', '계절의 꽃을 만나는 축제', 15],
    ['스포츠', 'genre', '운동과 야외 활동 중심의 축제', 12],
    ['영화', 'genre', '영화 상영과 관련 행사를 즐기는 축제', 8],
    ['체험', 'genre', '직접 참여하고 만들어 보는 축제', 19],
    ['야경', 'genre', '밤의 풍경과 조명을 즐기는 축제', 0],
    ['공예', 'genre', '공예품을 감상하고 만드는 축제', 0],
    ['혼자', 'target', '혼자 방문해도 즐기기 좋은 축제', 28],
    ['친구', 'target', '친구와 함께 즐기기 좋은 축제', 33],
    ['연인', 'target', '연인과 함께하기 좋은 축제', 20],
    ['가족', 'target', '가족과 함께 즐기기 좋은 축제', 31],
    ['아이와 함께', 'target', '아이와 함께 체험하기 좋은 축제', 14]
].map(function (item, index) {
    return { id: index + 1, name: item[0], type: item[1], description: item[2], usage: item[3], isPublic: true, color: tagPalette[index % tagPalette.length] };
});
// 화면 상태: 다음 더미 ID, 선택 탭·검색어·페이지, 수정 대상 ID.
// editingTagId가 null이면 추가 모드, 숫자이면 해당 태그 수정 모드다.
// 실제 ID는 서버·DB가 발급한다. nextTagId는 시연용이다.
let nextTagId = tags.length + 1;
let selectedTagType = 'all';
let selectedTagStatus = 'all';
let tagKeyword = '';
let tagPage = 1;
let tagSortField = null;
let tagSortDirection = 'ascending';
const tagNameCollator = new Intl.Collator('ko', { numeric: true, sensitivity: 'base' });
let editingTagId = null;
// 태그 화면의 실제 페이지당 표시 개수. 개수를 바꾸려면 이 값만 수정한다.
const tagsPerPage = 10;
// tags.html과 연결하는 요소들. HTML ID를 변경하면 이 선택자도 같이 변경한다.
const tagDialog = document.querySelector('#tag-dialog');
const tagForm = document.querySelector('#tag-form');
const tagNameInput = document.querySelector('#tag-name');
const tagTypeInput = document.querySelector('#tag-type');
const tagStatusInput = document.querySelector('#tag-status');
const tagColorInput = document.querySelector('#tag-color');
const tagColorPalette = document.querySelector('#tag-color-palette');
const tagDescription = document.querySelector('#tag-description');
const tagTabs = Array.from(document.querySelectorAll('[data-tag-type]'));

// 반복되는 요소 생성을 줄이는 도우미. 사용자 입력은 innerHTML 대신 textContent로 넣는다.
// 태그명·설명에 HTML 같은 문자열을 입력해도 실행하지 않고 글자로 표시하기 위해서다.
function tagElement(tagName, className, text) {
    const element = document.createElement(tagName);
    element.className = className;
    if (text !== undefined) element.textContent = text;
    return element;
}

// 파스텔 배경의 색상 계열을 유지하면서 글자색을 자동으로 짙게 만든다.
function getTagTextColor(color) {
    return '#' + [1, 3, 5].map(function (offset) {
        const channel = parseInt(color.slice(offset, offset + 2), 16);
        return Math.round(channel * 0.3).toString(16).padStart(2, '0');
    }).join('').toUpperCase();
}

// 종류·검색·정렬 조건을 적용한 전체 결과. 화면 표시와 저장 후 페이지 계산에 함께 쓴다.
function getTagResults() {
    const results = tags.filter(function (tag) {
        return (selectedTagType === 'all' || tag.type === selectedTagType)
            && (selectedTagStatus === 'all' || (tag.isPublic === false ? 'hidden' : 'visible') === selectedTagStatus)
            && tag.name.toLocaleLowerCase().includes(tagKeyword);
    });
    // 페이지를 나누기 전에 전체 결과를 정렬한다. 같은 값은 ID순으로 유지한다.
    if (tagSortField) {
        results.sort(function (first, second) {
            const difference = tagSortField === 'name'
                ? tagNameCollator.compare(first.name, second.name)
                : first.usage - second.usage;
            return difference === 0 ? first.id - second.id
                : difference * (tagSortDirection === 'ascending' ? 1 : -1);
        });
    }
    return results;
}

/**
 * 전체 집계 → 종류·검색·정렬 → 페이지 범위 보정 → 현재 페이지 행 생성
 * → 건수와 페이지 버튼 갱신 순서로 화면을 다시 그린다.
 * 행을 새로 만들기 때문에 수정 버튼 이벤트도 여기서 연결한다. 상태는 수정 모달에서 변경한다.
 */
function renderTags() {
    document.querySelectorAll('[data-tag-status-filter]').forEach(function (button) {
        button.setAttribute('aria-pressed', String(button.dataset.tagStatusFilter === selectedTagStatus));
    });
    document.querySelector('#tag-genre-count').textContent = tags.filter(function (tag) { return tag.type === 'genre'; }).length;
    document.querySelector('#tag-target-count').textContent = tags.filter(function (tag) { return tag.type === 'target'; }).length;
    document.querySelector('#tag-total-count').textContent = tags.length;
    const category = tags.filter(function (tag) { return selectedTagType === 'all' || tag.type === selectedTagType; });
    const results = getTagResults();
    // 공통 함수가 페이지별 데이터 분리와 결과 개수 변경 시 페이지 범위 보정을 맡는다.
    const pageData = paginateItems(results, tagPage, tagsPerPage);
    tagPage = pageData.currentPage;
    const body = document.querySelector('#tag-list');
    body.replaceChildren();

    // 필터링된 결과 전체 중 현재 페이지의 데이터만 잘라서 표시한다.
    pageData.items.forEach(function (tag) {
        const row = document.createElement('tr');
        const statusCell = document.createElement('td');
        const isPublic = tag.isPublic !== false;
        statusCell.append(tagElement('span', `badge tag-status-${isPublic ? 'visible' : 'hidden'}`, isPublic ? '공개' : '비공개'));
        const nameCell = document.createElement('td');
        const nameBadge = tagElement('span', 'tag-name-badge', tag.name);
        // 태그별 색상은 요소에 직접 적용하며 공통 CSS 변수로 등록하지 않는다.
        nameBadge.style.backgroundColor = tag.color;
        nameBadge.style.color = getTagTextColor(tag.color);
        nameBadge.title = tag.name;
        nameCell.append(nameBadge);
        row.append(tagElement('td', '', tag.id), statusCell, nameCell,
            tagElement('td', 'tag-description text-start', tag.description || '—'), tagElement('td', '', tag.usage));
        // 각 행의 버튼은 해당 tag 객체를 기억한다. 수정은 같은 추가 모달을 재사용한다.
        const actions = document.createElement('td');
        const group = tagElement('div', 'tag-actions');
        const edit = tagElement('button', 'btn btn-outline-secondary btn-sm', '수정');
        edit.type = 'button';
        edit.setAttribute('aria-label', `${tag.name} 태그 수정`);
        edit.addEventListener('click', function () { openTagDialog(tag); });
        group.append(edit);
        actions.append(group);
        row.append(actions);
        body.append(row);
    });

    if (results.length === 0) {
        const row = document.createElement('tr');
        const cell = tagElement('td', 'py-5 text-secondary', '조건에 맞는 태그가 없습니다.');
        cell.colSpan = 6;
        row.append(cell);
        body.append(row);
    }
    document.querySelector('#tag-list-heading').textContent = `${tagTypeNames[selectedTagType]} 태그 목록`;
    document.querySelector('#tag-result-count').textContent = `전체 ${category.length}건 · 검색 결과 ${results.length}건`;
    // 공통 버튼은 페이지 변경만 알려주고, 태그 화면이 자체 목록을 다시 그린다.
    renderPagination({
        containerSelector: '#tag-pagination',
        pagesSelector: '#tag-pages',
        currentPage: pageData.currentPage,
        pageCount: pageData.pageCount,
        totalCount: pageData.totalCount,
        onPageChange: function (page) {
            tagPage = page;
            renderTags();
        }
    });
}

// 종류 변경 시 페이지는 1로 초기화하지만 검색어는 유지한다.
// active는 스타일, aria-selected·tabIndex는 보조기기와 키보드용 선택 상태다.
function selectTagType(type) {
    selectedTagType = type;
    tagPage = 1;
    tagTabs.forEach(function (tab) {
        const active = tab.dataset.tagType === type;
        tab.classList.toggle('active', active);
        tab.setAttribute('aria-selected', String(active));
        tab.tabIndex = active ? 0 : -1;
        if (active) document.querySelector('#tag-panel').setAttribute('aria-labelledby', tab.id);
    });
    renderTags();
}

// 색상을 누르면 숨겨진 입력값과 선택 표시만 바꾼다. 저장 전에는 목록에 반영하지 않는다.
function renderTagPalette() {
    tagColorPalette.replaceChildren();
    const colors = tagPalette.includes(tagColorInput.value) ? tagPalette : [...tagPalette, tagColorInput.value];
    colors.forEach(function (color, index) {
        const selected = color === tagColorInput.value;
        const name = tagPaletteNames[index] || '기존 색상';
        const button = tagElement('button', 'tag-color-option', selected ? '✓' : '');
        button.type = 'button';
        button.style.backgroundColor = color;
        button.style.color = getTagTextColor(color);
        button.title = name;
        button.setAttribute('aria-label', name);
        button.setAttribute('aria-pressed', String(selected));
        button.addEventListener('click', function () {
            tagColorInput.value = color;
            renderTagPalette();
            // 다시 생성된 동일 버튼으로 포커스를 복원한다.
            tagColorPalette.children[index].focus();
        });
        tagColorPalette.append(button);
        if (selected) document.querySelector('#tag-color-selection').textContent = `${name} 선택됨`;
    });
}

// 인자가 없으면 새 태그, tag 객체가 있으면 수정 모드로 같은 모달을 연다.
// 이전 입력·오류를 지우고 기본값 또는 기존 값을 채워 취소한 내용이 다음 작업에 남지 않게 한다.
// showModal()은 배경 클릭 차단·포커스 제한을 맡고, Escape 닫기는 dialog 기본 동작이다.
function openTagDialog(tag) {
    editingTagId = tag ? tag.id : null;
    tagForm.reset();
    tagNameInput.setCustomValidity('');
    tagNameInput.value = tag ? tag.name : '';
    // '전체'는 조회용 탭이므로 새 태그의 기본 종류는 실제 값인 '축제 장르'로 설정한다.
    tagTypeInput.value = tag ? tag.type : selectedTagType === 'all' ? 'genre' : selectedTagType;
    tagColorInput.value = tag ? tag.color : tagPalette[0];
    renderTagPalette();
    tagDescription.value = tag ? tag.description : '';
    // 새 태그는 공개가 기본값이며 수정 시 기존 공개 상태를 그대로 표시한다.
    tagStatusInput.value = tag && tag.isPublic === false ? 'hidden' : 'visible';
    document.querySelector('#tag-dialog-title').textContent = tag ? '태그 수정' : '태그 추가';
    document.querySelector('#tag-save').textContent = tag ? '저장' : '태그 추가';
    document.querySelector('#tag-dialog-close').setAttribute('aria-label', tag ? '태그 수정 창 닫기' : '태그 추가 창 닫기');
    tagDialog.showModal();
}

// 정렬 컬럼을 바꾸면 오름차순으로 시작한다. 기존 탭·검색은 유지하고 페이지는 1로 되돌린다.
const tagSortButtons = document.querySelectorAll('[data-tag-sort]');
tagSortButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        const field = button.dataset.tagSort;
        tagSortDirection = tagSortField === field && tagSortDirection === 'ascending' ? 'descending' : 'ascending';
        tagSortField = field;
        tagPage = 1;
        tagSortButtons.forEach(function (item) {
            const active = item === button;
            item.closest('th').setAttribute('aria-sort', active ? tagSortDirection : 'none');
            item.querySelector('span').textContent = active ? (tagSortDirection === 'ascending' ? '↑' : '↓') : '↕';
        });
        renderTags();
    });
});

// 클릭과 키보드(좌우 방향키·Home·End)로 탭을 선택할 수 있도록 연결한다.
tagTabs.forEach(function (tab, index) {
    tab.addEventListener('click', function () { selectTagType(tab.dataset.tagType); });
    tab.addEventListener('keydown', function (event) {
        if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return;
        event.preventDefault();
        const next = event.key === 'Home' ? 0 : event.key === 'End' ? tagTabs.length - 1
            : (index + (event.key === 'ArrowRight' ? 1 : -1) + tagTabs.length) % tagTabs.length;
        tagTabs[next].focus();
        selectTagType(tagTabs[next].dataset.tagType);
    });
});

// 검색 버튼과 Enter를 같은 이벤트로 처리하며 새 검색은 항상 1페이지부터 보여준다.
document.querySelector('#tag-search-form').addEventListener('submit', function (event) {
    event.preventDefault();
    tagKeyword = document.querySelector('#tag-search').value.trim().toLocaleLowerCase();
    tagPage = 1;
    renderTags();
});
// X와 취소는 모달만 닫는다. tags 배열은 제출 시에만 추가·수정한다.
document.querySelector('#tag-add-button').addEventListener('click', function () { openTagDialog(); });
document.querySelector('#tag-dialog-close').addEventListener('click', function () { tagDialog.close(); });
document.querySelector('#tag-dialog-cancel').addEventListener('click', function () { tagDialog.close(); });
// 이름이나 종류를 고치면 이전 중복 오류를 해제하고 제출 시 다시 검사한다.
tagNameInput.addEventListener('input', function () { tagNameInput.setCustomValidity(''); });
tagTypeInput.addEventListener('change', function () { tagNameInput.setCustomValidity(''); });
/**
 * 저장 흐름: 입력 정리 → 필수값·색상·중복 검증 → 배열 추가/수정 → 모달 닫기
 * → 해당 종류와 저장한 태그의 페이지 표시 → 완료 메시지.
 * 서버 연동 시 검증 뒤 API를 호출하고 성공한 경우에만 모달을 닫고 화면을 갱신한다.
 * 중복과 색상 형식 검증은 서버에서도 수행해야 한다.
 */
tagForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const name = tagNameInput.value.trim();
    const type = tagTypeInput.value;
    const color = tagColorInput.value;
    // 수정 대상 자기 자신은 제외한다. 같은 종류 안에서만 중복을 막고 다른 종류는 허용한다.
    const duplicate = tags.some(function (tag) {
        return tag.id !== editingTagId && tag.type === type && tag.name.toLocaleLowerCase() === name.toLocaleLowerCase();
    });
    tagNameInput.setCustomValidity(!name ? '태그명을 입력해 주세요.' : duplicate ? '같은 종류에 이미 등록된 태그명입니다.' : '');
    if (!/^#[0-9a-f]{6}$/i.test(color)) {
        document.querySelector('#tag-color-selection').textContent = '팔레트에서 색상을 선택해 주세요.';
        return;
    }
    if (!tagForm.reportValidity()) return;
    // 현재는 메모리 배열만 변경한다. 수정 시 기존 ID와 사용 축제 수를 유지한다.
    const existing = tags.find(function (tag) { return tag.id === editingTagId; });
    const item = { id: existing ? existing.id : nextTagId++, name, type, color, isPublic: tagStatusInput.value === 'visible', description: tagDescription.value.trim(), usage: existing ? existing.usage : 0 };
    if (existing) Object.assign(existing, item);
    else tags.push(item);
    // 저장한 항목이 필터에 가려지지 않도록 선택된 상태 필터도 저장한 상태에 맞춘다.
    if (selectedTagStatus !== 'all') selectedTagStatus = item.isPublic ? 'visible' : 'hidden';
    // 저장한 항목이 검색 조건 때문에 숨지 않도록 검색을 비우고 해당 탭으로 이동한다.
    tagKeyword = '';
    document.querySelector('#tag-search').value = '';
    tagDialog.close();
    selectTagType(type);
    // 저장 후에도 현재 정렬 기준에 맞는 위치로 이동한다.
    const category = getTagResults();
    // 배열 위치는 0부터, 페이지는 1부터 시작한다. 저장한 항목이 있는 페이지를 계산한다.
    tagPage = Math.floor(category.findIndex(function (tag) { return tag.id === item.id; }) / tagsPerPage) + 1;
    renderTags();
    document.querySelector('#tag-feedback').textContent = `‘${name}’ 태그를 ${existing ? '수정' : '추가'}했습니다.`;
});

// 공개 상태 필터는 종류·검색·정렬 조건과 함께 적용하고 변경 시 1페이지로 돌아간다.
const tagStatusFilter = document.querySelector('#tag-status-filter');
tagStatusFilter.querySelectorAll('[data-tag-status-filter]').forEach(function (button) {
    button.addEventListener('click', function () {
        selectedTagStatus = button.dataset.tagStatusFilter;
        tagPage = 1;
        renderTags();
        tagStatusFilter.open = false;
        tagStatusFilter.querySelector('summary').focus();
    });
});
document.addEventListener('click', function (event) {
    if (!tagStatusFilter.contains(event.target)) tagStatusFilter.open = false;
});
tagStatusFilter.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        tagStatusFilter.open = false;
        tagStatusFilter.querySelector('summary').focus();
    }
});

// 이벤트 연결을 마친 뒤 초기 집계·목록·페이지 버튼을 표시한다.
renderTags();
