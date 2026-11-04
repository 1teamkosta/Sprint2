/**
 * 축제 추가 UI: 이미지 미리보기 → 태그 모달 검색·선택 → 입력 검증.
 * 태그는 시연용이며 서버 연동 시 공개된 태그 목록으로 교체한다.
 * 저장은 검증 결과만 안내한다. 파일·브라우저 저장소·DB에는 저장하지 않는다.
 */
const festivalTagCatalog = [
    ...['음악', '도시', '먹거리', '전통문화', '예술', '자연', '꽃', '스포츠', '영화', '체험', '야경', '공예'].map(function (name, index) {
        return { id: index + 1, name, type: '축제 장르' };
    }),
    ...['혼자', '친구', '연인', '가족', '아이와 함께'].map(function (name, index) {
        return { id: index + 13, name, type: '추천 대상' };
    })
];
const selectedFestivalTags = new Set();
const festivalTagDialog = document.querySelector('#festival-tag-dialog');
const festivalTagSearch = document.querySelector('#festival-tag-search');
const festivalTagOptions = document.querySelector('#festival-tag-options');
const festivalTagContainer = document.querySelector('#festival-selected-tags');
const festivalGenre = document.querySelector('#festival-genre');
const festivalStartDate = document.querySelector('#festival-start-date');
const festivalEndDate = document.querySelector('#festival-end-date');

festivalTagCatalog.filter(function (tag) { return tag.type === '축제 장르'; }).forEach(function (tag) {
    const option = document.createElement('option');
    option.value = tag.name;
    option.textContent = tag.name;
    festivalGenre.append(option);
});

// 모달의 선택 상태와 본문의 선택 태그를 함께 갱신한다. 텍스트는 textContent로 넣는다.
function renderFestivalTagOptions() {
    const keyword = festivalTagSearch.value.trim().toLocaleLowerCase();
    const results = festivalTagCatalog.filter(function (tag) { return tag.name.toLocaleLowerCase().includes(keyword); });
    festivalTagOptions.replaceChildren();
    results.forEach(function (tag) {
        const row = document.createElement('tr');
        [tag.type, tag.name].forEach(function (value) {
            const cell = document.createElement('td');
            cell.textContent = value;
            row.append(cell);
        });
        const actions = document.createElement('td');
        actions.className = 'text-end';
        const button = document.createElement('button');
        const selected = selectedFestivalTags.has(tag.id);
        button.type = 'button';
        button.className = 'btn btn-outline-primary btn-sm';
        button.textContent = selected ? '선택됨' : '선택';
        button.disabled = selected;
        button.setAttribute('aria-label', `${tag.name} 태그 ${selected ? '선택됨' : '선택'}`);
        button.addEventListener('click', function () {
            selectedFestivalTags.add(tag.id);
            renderSelectedFestivalTags();
            renderFestivalTagOptions();
            // 선택 버튼이 비활성화되므로 검색창으로 포커스를 돌려 다음 선택을 이어간다.
            festivalTagSearch.focus();
        });
        actions.append(button);
        row.append(actions);
        festivalTagOptions.append(row);
    });
    document.querySelector('#festival-tag-feedback').textContent = results.length ? `검색 결과 ${results.length}개 · 선택한 태그 ${selectedFestivalTags.size}개` : '검색 결과가 없습니다.';
}
function renderSelectedFestivalTags() {
    festivalTagContainer.replaceChildren();
    if (!selectedFestivalTags.size) {
        const empty = document.createElement('span');
        empty.className = 'small text-secondary';
        empty.textContent = '선택한 태그가 없습니다.';
        festivalTagContainer.append(empty);
    }
    festivalTagCatalog.filter(function (tag) { return selectedFestivalTags.has(tag.id); }).forEach(function (tag) {
        const chip = document.createElement('span');
        chip.className = 'festival-selected-tag';
        const label = document.createElement('span');
        label.textContent = tag.name;
        const remove = document.createElement('button');
        remove.type = 'button';
        remove.textContent = '×';
        remove.setAttribute('aria-label', `${tag.name} 태그 제거`);
        remove.addEventListener('click', function () {
            selectedFestivalTags.delete(tag.id);
            renderSelectedFestivalTags();
            document.querySelector('#festival-tag-open').focus();
        });
        chip.append(label, remove);
        festivalTagContainer.append(chip);
    });
}
document.querySelector('#festival-tag-open').addEventListener('click', function () {
    festivalTagSearch.value = '';
    renderFestivalTagOptions();
    festivalTagDialog.showModal();
});
document.querySelector('#festival-tag-close').addEventListener('click', function () { festivalTagDialog.close(); });
festivalTagSearch.addEventListener('input', renderFestivalTagOptions);
// dialog의 기본 동작으로 Escape 닫기와 모달 외부 포커스 차단을 제공한다.

// 선택 이미지의 임시 URL만 사용하고, 교체·페이지 종료 시 해제한다.
let festivalPreviewUrl = null;
document.querySelector('#festival-image').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const preview = document.querySelector('#festival-image-preview');
    const placeholder = document.querySelector('#festival-image-placeholder');
    const feedback = document.querySelector('#festival-image-feedback');
    if (festivalPreviewUrl) URL.revokeObjectURL(festivalPreviewUrl);
    festivalPreviewUrl = null;
    preview.hidden = true;
    preview.removeAttribute('src');
    placeholder.hidden = false;
    feedback.textContent = '';
    if (!file) return;
    if (!file.type.startsWith('image/')) {
        event.target.value = '';
        feedback.textContent = '이미지 파일을 선택해 주세요.';
        return;
    }
    festivalPreviewUrl = URL.createObjectURL(file);
    preview.src = festivalPreviewUrl;
    preview.hidden = false;
    placeholder.hidden = true;
    feedback.textContent = file.name;
});
window.addEventListener('pagehide', function () { if (festivalPreviewUrl) URL.revokeObjectURL(festivalPreviewUrl); });

function validateFestivalDates() {
    festivalEndDate.min = festivalStartDate.value;
    festivalEndDate.setCustomValidity(festivalStartDate.value && festivalEndDate.value && festivalEndDate.value < festivalStartDate.value ? '종료일은 시작일 이후로 선택해 주세요.' : '');
}
festivalStartDate.addEventListener('input', validateFestivalDates);
festivalEndDate.addEventListener('input', validateFestivalDates);
document.querySelector('#festival-add-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const title = document.querySelector('#festival-name');
    const place = document.querySelector('#festival-place');
    [title, place].forEach(function (input) { input.value = input.value.trim(); });
    validateFestivalDates();
    if (!event.currentTarget.reportValidity()) return;
    const feedback = document.querySelector('#festival-save-feedback');
    feedback.textContent = document.body.dataset.festivalMode === 'edit'
        ? '입력 확인이 완료되었습니다. 현재 화면에서는 저장되지 않으며, 실제 수정은 서버 연동 후 가능합니다.'
        : '입력 확인이 완료되었습니다. 현재 화면에서는 저장되지 않으며, 실제 등록은 서버 연동 후 가능합니다.';
    feedback.hidden = false;
});
renderSelectedFestivalTags();
