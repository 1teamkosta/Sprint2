/**
 * 추가 페이지의 CSS·태그 모달·이미지 미리보기·검증을 재사용한다.
 * 수정 화면에서는 URL의 id로 목록 더미 데이터를 찾아 입력란에 채운다.
 * 서버 연동 시 조회 API와 수정 API로 교체하며 현재 변경은 저장되지 않는다.
 */
async function loadFestivalEdit() {
    const festivalId = new URLSearchParams(window.location.search).get('id') || '1';
    const feedback = document.querySelector('#festival-edit-feedback');
    document.querySelector('#festival-edit-cancel').href = `festivaldetail.html?id=${encodeURIComponent(festivalId)}`;
    const saveLink = document.querySelector('#festival-edit-save');
    saveLink.href = `festivaldetail.html?id=${encodeURIComponent(festivalId)}`;
    try {
        const response = await fetch('festivals.html');
        if (!response.ok) throw new Error('축제 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const row = Array.from(listDocument.querySelectorAll('tbody tr[data-status]')).find(function (item) {
            return item.querySelector('[data-field="festivalId"]').textContent.trim() === festivalId;
        });
        if (!row) throw new Error('해당 축제를 찾을 수 없습니다.');
        const title = row.querySelector('[data-field="title"]').textContent.trim();
        const genre = row.querySelector('[data-field="genre"]').textContent.trim();
        const place = row.querySelector('[data-field="place"]').textContent.trim();
        const dates = row.querySelectorAll('[data-sort-field="startDate"] time');
        document.querySelector('#festival-name').value = title;
        document.querySelector('#festival-place').value = place;
        festivalStartDate.value = dates[0].getAttribute('datetime');
        festivalEndDate.value = dates[1].getAttribute('datetime');
        // 기존 데이터의 장르가 현재 선택지에 없어도 원래 값을 표시한다.
        if (!Array.from(festivalGenre.options).some(function (option) { return option.value === genre; })) {
            const option = document.createElement('option');
            option.value = genre;
            option.textContent = genre;
            festivalGenre.append(option);
        }
        festivalGenre.value = genre;
        document.querySelector('#festival-description').value = `${title}에서 다양한 ${genre} 프로그램을 만나 보세요.\n${place}에서 축제메이트와 함께 즐거운 추억을 만들어 보세요.`;
        // 추천 대상은 상세 화면에 표시되는 태그의 시연용 초기값이다.
        festivalTagCatalog.filter(function (tag) { return ['친구', '연인', '가족'].includes(tag.name); }).forEach(function (tag) {
            selectedFestivalTags.add(tag.id);
        });
        renderSelectedFestivalTags();
        validateFestivalDates();
        document.title = `${title} 수정 | 축제메이트`;
        document.querySelector('#festival-edit-fields').disabled = false;
        // 현재 저장 링크는 상세로 이동만 한다. 실제 수정 처리는 서버 연동 시 연결한다.
        saveLink.classList.remove('disabled');
        saveLink.removeAttribute('aria-disabled');
        saveLink.removeAttribute('tabindex');
        feedback.hidden = true;
    } catch (error) {
        feedback.textContent = '축제 정보를 불러오지 못했습니다. 목록에서 축제를 다시 선택해 주세요.';
        document.querySelector('#festival-edit-cancel').href = 'festivals.html';
        console.error(error);
    }
}
loadFestivalEdit();
