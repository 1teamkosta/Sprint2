/**
 * 제목과 작성일은 공지 목록의 더미 행을 재사용하고 본문은 공지별 예시로 채운다.
 * 서버 연동 시 URL의 id로 공지 상세를 조회하도록 교체한다.
 */
const noticeDemoContents = {
    '1': [
        '안녕하세요. 축제메이트입니다.',
        '관심 있는 축제를 찾아보고, 함께 즐길 모임에 참여하는 방법을 안내드립니다.',
        '축제 목록에서 장르와 제목으로 축제를 찾고, 상세 화면에서 일정과 장소를 확인해 주세요.',
        '함께 공연을 즐기고 싶다면 축제메이트 모임을, 먹거리를 나누고 싶다면 음식메이트 모임을 찾아보세요.',
        '축제메이트와 함께 즐거운 축제 경험을 만들어 보세요. 감사합니다.'
    ],
    '2': ['안녕하세요. 축제메이트입니다.', '더 안정적인 서비스 제공을 위한 점검을 진행할 예정입니다.', '점검 시간에는 일부 기능의 이용이 제한될 수 있습니다. 상세 일정은 추후 안내드리겠습니다.', '이용해 주셔서 감사합니다.'],
    '3': ['즐거운 축제 동행을 위해 모임의 목적과 일정을 미리 확인해 주세요.', '만남 장소와 시간을 참여자끼리 충분히 이야기하고, 서로의 의견을 존중해 주세요.', '불편한 상황이 생겼다면 신고 및 문의 기능을 이용해 주세요.'],
    '4': ['축제 일정과 장소 등 일부 정보가 변경될 수 있어 안내드립니다.', '방문 전 축제 상세 화면과 주최 측의 최신 안내를 함께 확인해 주세요.', '변경된 정보는 확인 후 순차적으로 반영하겠습니다.'],
    '5': ['다양한 축제 먹거리를 함께 나누고 싶다면 음식메이트 모임을 이용해 보세요.', '모임에 참여하기 전에 나눠 먹을 메뉴, 인원, 만남 장소를 확인해 주세요.', '음식 취향과 알레르기 등은 참여자끼리 미리 이야기해 주세요.'],
    '6': ['개인정보 처리방침 개정 안내를 위한 예시 공지입니다.', '변경 항목과 적용 일정은 확정 후 이 공지를 통해 안내드릴 예정입니다.', '서비스 이용에 참고해 주세요.'],
    '7': ['참여한 축제의 경험을 후기로 남겨 주세요.', '공연, 먹거리, 장소 등 직접 경험한 내용을 작성하면 다른 이용자의 축제 선택에 도움이 됩니다.', '서로를 배려하는 표현으로 후기를 작성해 주세요.'],
    '8': ['관심 있는 장르와 관련 태그로 축제를 찾아보세요.', '축제 상세에서 장르와 관련 태그를 확인할 수 있습니다.', '선호하는 축제를 찜하고 다음 축제 계획에 활용해 보세요.'],
    '9': ['서비스를 이용하며 불편한 점이 있다면 문의를 남겨 주세요.', '문의할 때 관련 축제나 모임 이름과 상황을 함께 작성하면 확인에 도움이 됩니다.', '접수된 내용은 순서대로 확인하겠습니다.'],
    '10': ['가을 축제에 방문하기 전 일정과 장소를 확인해 주세요.', '야외 행사라면 날씨와 귀가 교통편을 미리 확인해 주세요.', '현장의 운영 안내에 따라 즐거운 시간을 보내시기 바랍니다.'],
    '11': ['축제메이트 서비스가 시작되었습니다.', '취향에 맞는 축제를 찾고 함께 즐길 동행을 만나 보세요.', '여러분의 축제 경험을 함께 만들어 가겠습니다. 감사합니다.']
};
async function loadNoticeDetail() {
    const noticeId = new URLSearchParams(window.location.search).get('id') || '1';
    const feedback = document.querySelector('#notice-detail-feedback');
    try {
        const response = await fetch('notices.html');
        if (!response.ok) throw new Error('공지 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const row = Array.from(listDocument.querySelectorAll('tbody tr[data-status]')).find(function (item) {
            return item.querySelector('[data-field="noticeId"]').textContent.trim() === noticeId;
        });
        if (!row || !noticeDemoContents[noticeId]) throw new Error('해당 공지를 찾을 수 없습니다.');
        const title = row.querySelector('[data-field="title"]').textContent.trim();
        const created = row.querySelector('[data-sort-field="createdAt"] time');
        document.title = `${title} | 축제메이트`;
        document.querySelector('#notice-detail-title').textContent = title;
        const date = document.querySelector('#notice-detail-created');
        date.textContent = created.textContent;
        date.setAttribute('datetime', created.getAttribute('datetime'));
        const content = document.querySelector('#notice-detail-content');
        content.replaceChildren();
        noticeDemoContents[noticeId].forEach(function (paragraph) {
            const item = document.createElement('p');
            item.textContent = paragraph;
            content.append(item);
        });
    } catch (error) {
        document.querySelector('.notice-detail-card').hidden = true;
        document.querySelector('#notice-delete').disabled = true;
        document.querySelector('#notice-edit').disabled = true;
        feedback.textContent = '공지 정보를 표시할 수 없습니다. 목록에서 다시 선택해 주세요.';
        feedback.hidden = false;
        console.error(error);
    }
}
loadNoticeDetail();
