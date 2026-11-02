// 태그 관리 시연용 데이터: 새로고침하면 초기 데이터로 돌아온다.
// 태그 색상은 공통 테마 변수가 아닌 개별 데이터로 취급한다.
const tagPalette = ['#FFF0AD', '#FBE5D6', '#D9EEE5', '#ECE5F5', '#DCEAF5', '#F1DCE5'];
const tagTypeNames = { genre: '축제 장르', target: '추천 대상' };
//DB연결 후 삭제 예정
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
    return { id: index + 1, name: item[0], type: item[1], description: item[2], usage: item[3], color: tagPalette[index % tagPalette.length] };
});
let nextTagId = tags.length + 1;
let selectedTagType = 'genre';
let tagKeyword = '';
let tagPage = 1;
let editingTagId = null;
const tagsPerPage = 10;
const tagDialog = document.querySelector('#tag-dialog');
const tagForm = document.querySelector('#tag-form');
const tagNameInput = document.querySelector('#tag-name');
const tagTypeInput = document.querySelector('#tag-type');
const tagColorInput = document.querySelector('#tag-color');
const tagColorCode = document.querySelector('#tag-color-code');
const tagDescription = document.querySelector('#tag-description');
const tagTabs = Array.from(document.querySelectorAll('[data-tag-type]'));

function tagElement(tagName, className, text) {
    const element = document.createElement(tagName);
    element.className = className;
    if (text !== undefined) element.textContent = text;
    return element;
}

function renderTags() {
    document.querySelector('#tag-genre-count').textContent = tags.filter(function (tag) { return tag.type === 'genre'; }).length;
    document.querySelector('#tag-target-count').textContent = tags.filter(function (tag) { return tag.type === 'target'; }).length;
    document.querySelector('#tag-total-count').textContent = tags.length;
    const category = tags.filter(function (tag) { return tag.type === selectedTagType; });
    const results = category.filter(function (tag) { return tag.name.toLocaleLowerCase().includes(tagKeyword); });
    const pageCount = Math.max(1, Math.ceil(results.length / tagsPerPage));
    tagPage = Math.min(tagPage, pageCount);
    const body = document.querySelector('#tag-list');
    body.replaceChildren();

    results.slice((tagPage - 1) * tagsPerPage, tagPage * tagsPerPage).forEach(function (tag) {
        const row = document.createElement('tr');
        row.append(tagElement('td', '', tag.id), tagElement('td', 'fw-medium', tag.name));
        const colorCell = document.createElement('td');
        const colorDisplay = tagElement('span', 'tag-color-display');
        const swatch = tagElement('span', 'tag-color-swatch');
        // 선택한 색상은 견본 요소에 직접 적용한다.
        swatch.style.backgroundColor = tag.color;
        swatch.setAttribute('aria-hidden', 'true');
        colorDisplay.append(swatch, tagElement('span', 'tag-code', tag.color));
        colorCell.append(colorDisplay);
        row.append(colorCell, tagElement('td', 'tag-description text-start', tag.description || '—'), tagElement('td', '', tag.usage));
        const actions = document.createElement('td');
        const group = tagElement('div', 'tag-actions');
        const edit = tagElement('button', 'btn btn-outline-secondary btn-sm', '수정');
        edit.type = 'button';
        edit.setAttribute('aria-label', `${tag.name} 태그 수정`);
        edit.addEventListener('click', function () { openTagDialog(tag); });
        const remove = tagElement('button', 'btn btn-outline-secondary btn-sm', '삭제');
        remove.type = 'button';
        remove.setAttribute('aria-label', `${tag.name} 태그 삭제`);
        remove.addEventListener('click', function () {
            if (tag.usage > 0) {
                document.querySelector('#tag-feedback').textContent = '축제에서 사용 중인 태그는 삭제할 수 없습니다.';
                return;
            }
            if (!window.confirm(`‘${tag.name}’ 태그를 삭제할까요?`)) return;
            tags = tags.filter(function (item) { return item.id !== tag.id; });
            document.querySelector('#tag-feedback').textContent = `‘${tag.name}’ 태그를 삭제했습니다.`;
            renderTags();
        });
        group.append(edit, remove);
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
    renderTagPages(pageCount, results.length);
}

function renderTagPages(pageCount, resultCount) {
    const pages = document.querySelector('#tag-pages');
    pages.replaceChildren();
    document.querySelector('#tag-pagination').hidden = resultCount === 0;
    function pageButton(label, page, disabled, active) {
        const item = tagElement('li', `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`);
        const button = tagElement('button', 'page-link', label);
        button.type = 'button';
        button.disabled = disabled;
        button.setAttribute('aria-label', typeof label === 'number' ? `${label}페이지` : `${label} 페이지`);
        if (active) button.setAttribute('aria-current', 'page');
        button.addEventListener('click', function () {
            tagPage = page;
            renderTags();
            const newButtons = document.querySelectorAll('#tag-pages button');
            newButtons[tagPage].focus();
        });
        item.append(button);
        pages.append(item);
    }
    pageButton('이전', tagPage - 1, tagPage === 1, false);
    for (let page = 1; page <= pageCount; page += 1) pageButton(page, page, false, page === tagPage);
    pageButton('다음', tagPage + 1, tagPage === pageCount, false);
}

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

function openTagDialog(tag) {
    editingTagId = tag ? tag.id : null;
    tagForm.reset();
    tagNameInput.setCustomValidity('');
    tagColorCode.setCustomValidity('');
    tagNameInput.value = tag ? tag.name : '';
    tagTypeInput.value = tag ? tag.type : selectedTagType;
    tagColorInput.value = tag ? tag.color : tagPalette[0];
    tagColorCode.value = tag ? tag.color : tagPalette[0];
    tagDescription.value = tag ? tag.description : '';
    document.querySelector('#tag-dialog-title').textContent = tag ? '태그 수정' : '태그 추가';
    document.querySelector('#tag-save').textContent = tag ? '저장' : '태그 추가';
    document.querySelector('#tag-dialog-close').setAttribute('aria-label', tag ? '태그 수정 창 닫기' : '태그 추가 창 닫기');
    tagDialog.showModal();
}

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

document.querySelector('#tag-search-form').addEventListener('submit', function (event) {
    event.preventDefault();
    tagKeyword = document.querySelector('#tag-search').value.trim().toLocaleLowerCase();
    tagPage = 1;
    renderTags();
});
document.querySelector('#tag-add-button').addEventListener('click', function () { openTagDialog(); });
document.querySelector('#tag-dialog-close').addEventListener('click', function () { tagDialog.close(); });
document.querySelector('#tag-dialog-cancel').addEventListener('click', function () { tagDialog.close(); });
tagColorInput.addEventListener('input', function () {
    tagColorCode.value = tagColorInput.value.toUpperCase();
    tagColorCode.setCustomValidity('');
});
tagColorCode.addEventListener('input', function () {
    const color = tagColorCode.value.trim();
    const valid = /^#[0-9a-f]{6}$/i.test(color);
    tagColorCode.setCustomValidity(valid ? '' : '#으로 시작하는 6자리 색상 코드를 입력해 주세요.');
    if (valid) tagColorInput.value = color;
});
tagNameInput.addEventListener('input', function () { tagNameInput.setCustomValidity(''); });
tagTypeInput.addEventListener('change', function () { tagNameInput.setCustomValidity(''); });
tagForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const name = tagNameInput.value.trim();
    const type = tagTypeInput.value;
    const color = tagColorCode.value.trim().toUpperCase();
    const duplicate = tags.some(function (tag) {
        return tag.id !== editingTagId && tag.type === type && tag.name.toLocaleLowerCase() === name.toLocaleLowerCase();
    });
    tagNameInput.setCustomValidity(!name ? '태그명을 입력해 주세요.' : duplicate ? '같은 종류에 이미 등록된 태그명입니다.' : '');
    tagColorCode.setCustomValidity(/^#[0-9a-f]{6}$/i.test(color) ? '' : '#으로 시작하는 6자리 색상 코드를 입력해 주세요.');
    if (!tagForm.reportValidity()) return;
    const existing = tags.find(function (tag) { return tag.id === editingTagId; });
    const item = { id: existing ? existing.id : nextTagId++, name, type, color, description: tagDescription.value.trim(), usage: existing ? existing.usage : 0 };
    if (existing) Object.assign(existing, item);
    else tags.push(item);
    tagKeyword = '';
    document.querySelector('#tag-search').value = '';
    tagDialog.close();
    selectTagType(type);
    const category = tags.filter(function (tag) { return tag.type === type; });
    tagPage = Math.floor(category.findIndex(function (tag) { return tag.id === item.id; }) / tagsPerPage) + 1;
    renderTags();
    document.querySelector('#tag-feedback').textContent = `‘${name}’ 태그를 ${existing ? '수정' : '추가'}했습니다.`;
});

renderTags();
