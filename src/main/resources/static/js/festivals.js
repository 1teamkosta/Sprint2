const festivalToday = new Date();
const festivalTodayKey = `${festivalToday.getFullYear()}-${String(festivalToday.getMonth() + 1).padStart(2, '0')}-${String(festivalToday.getDate()).padStart(2, '0')}`;

function getProgressMeta(festival) {
    if (festival.progressStatus !== undefined && festival.progressStatus !== null && festival.progressStatus !== '') {
        const code = Number(festival.progressStatus);
        if (code === 0) return { key: 'ended', label: '종료', badgeClass: 'festival-progress-ended' };
        if (code === 2) return { key: 'upcoming', label: '예정', badgeClass: 'festival-progress-upcoming' };
        return { key: 'ongoing', label: '진행중', badgeClass: 'festival-progress-ongoing' };
    }

    const start = festival.startDate || '';
    const end = festival.endDate || '';
    if (start && festivalTodayKey < start) {
        return { key: 'upcoming', label: '예정', badgeClass: 'festival-progress-upcoming' };
    }
    if (end && festivalTodayKey > end) {
        return { key: 'ended', label: '종료', badgeClass: 'festival-progress-ended' };
    }
    return { key: 'ongoing', label: '진행중', badgeClass: 'festival-progress-ongoing' };
}

const festivalGenreColors = {
    '음악': '#FADADD',
    '먹거리': '#FFE0B2',
    '문화': '#FFF0AD'
};

function resolveFestivalTagColor(festival) {
    if (!festival || typeof festival !== 'object') return '';
    return (
        festival.color ||
        festival.mainTagColor ||
        festival.tagColor ||
        ''
    ).trim();
}

function getContrastTextColor(hexColor) {
    if (!hexColor || typeof hexColor !== 'string') return '#1F2937';
    const clean = hexColor.replace('#', '').trim();
    if (!/^[0-9a-fA-F]{6}$/.test(clean)) return '#1F2937';

    const r = parseInt(clean.slice(0, 2), 16);
    const g = parseInt(clean.slice(2, 4), 16);
    const b = parseInt(clean.slice(4, 6), 16);
    const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
    return luminance > 0.55 ? '#1F2937' : '#FFFFFF';
}

function applyGenreBadgeStyle(badge, explicitColor) {
    if (!badge) return;
    const genre = badge.textContent.trim();
    const color = (explicitColor || badge.dataset.tagColor || festivalGenreColors[genre] || '').trim();
    if (!color) return;

    badge.dataset.tagColor = color;
    badge.style.backgroundColor = color;
    badge.style.color = getContrastTextColor(color);
}

function applyInitialProgressAndGenre() {
    document.querySelectorAll('.festival-table tbody tr[data-status]').forEach(function (row) {
        const dates = row.querySelectorAll('[data-sort-field="startDate"] time');
        if (dates.length >= 2) {
            const start = dates[0].getAttribute('datetime') || '';
            const end = dates[1].getAttribute('datetime') || '';
            const progress = festivalTodayKey < start ? 'upcoming' : festivalTodayKey > end ? 'ended' : 'ongoing';
            row.dataset.progress = progress;
            const badge = row.querySelector('[data-festival-progress]');
            if (badge) {
                badge.textContent = { upcoming: '예정', ongoing: '진행중', ended: '종료' }[progress];
                badge.className = `badge festival-progress-${progress}`;
            }
        }
    });

    document.querySelectorAll('.festival-genre-tag').forEach(function (badge) {
        applyGenreBadgeStyle(badge);
    });
    shortenListText('.festival-title', 30);
    shortenListText('.festival-location', 15);
}

const festivalStatusFilterMap = {
    all: null,
    '1': 1,
    '0': 0
};

let activeFestivalStatusFilter = null;
let selectedFestivalProgress = '';

function changeFestivalQuery(changes) {
    let nextChanges = { ...changes };

    if (Object.prototype.hasOwnProperty.call(nextChanges, 'filterByStatus')) {
        nextChanges.filterByStatus = activeFestivalStatusFilter;
    }

    if (typeof nextChanges.keyword === 'string' && nextChanges.keyword.trim() === '') {
        nextChanges = {
            ...nextChanges,
            page: 1,
            searchBy: null,
            keyword: null
        };
    }

    const params = new URLSearchParams(window.location.search);

    Object.entries(nextChanges).forEach(function ([name, value]) {
        if (value == null || value === '') {
            params.delete(name);
        } else {
            params.set(name, value);
        }
    });

    params.delete('status');
    params.delete('progressStatus');

    if (activeFestivalStatusFilter != null) {
        params.set('filterByStatus', activeFestivalStatusFilter);
    } else if (!Object.prototype.hasOwnProperty.call(nextChanges, 'filterByStatus')) {
        params.delete('filterByStatus');
    }

    if (selectedFestivalProgress !== '') {
        params.set('filterByProgressStatus', selectedFestivalProgress);
    } else if (!Object.prototype.hasOwnProperty.call(nextChanges, 'filterByProgressStatus')) {
        params.delete('filterByProgressStatus');
    }

    return loadFestivals(params);
}

const festivalList = setupList({
    prefix: 'festival',
    tableSelector: '.festival-table',
    idField: 'festivalId',
    exactFields: ['festivalId'],
    onQueryChange: changeFestivalQuery
});

const festivalSelectAll = document.querySelector('#festival-select-all');

function syncFestivalSelectAllState() {
    if (!festivalSelectAll) return;
    const checkboxes = Array.from(
        document.querySelectorAll('#festival-list .list-selection-input')
    );
    if (checkboxes.length === 0) {
        festivalSelectAll.checked = false;
        festivalSelectAll.indeterminate = false;
        return;
    }
    const checkedCount = checkboxes.filter(function (cb) {
        return cb.checked;
    }).length;
    festivalSelectAll.checked = checkedCount === checkboxes.length;
    festivalSelectAll.indeterminate = checkedCount > 0 && checkedCount < checkboxes.length;
}

if (festivalSelectAll) {
    festivalSelectAll.addEventListener('change', function () {
        const isChecked = festivalSelectAll.checked;
        document.querySelectorAll('#festival-list .list-selection-input').forEach(function (cb) {
            cb.checked = isChecked;
        });
        festivalSelectAll.indeterminate = false;
    });
}

document.addEventListener('change', function (event) {
    if (event.target && event.target.matches('#festival-list .list-selection-input')) {
        syncFestivalSelectAllState();
    }
});

const festivalStatusFilter = document.querySelector('#festival-status-filter');
if (festivalStatusFilter) {
    const statusButtons = festivalStatusFilter.querySelectorAll('[data-status-filter]');
    statusButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const key = button.dataset.statusFilter;
            activeFestivalStatusFilter = festivalStatusFilterMap[key] ?? null;

            statusButtons.forEach(function (item) {
                const selected = item === button;
                item.setAttribute('aria-pressed', String(selected));
                item.classList.toggle('active', selected);
            });

            festivalStatusFilter.open = false;
            changeFestivalQuery({ page: 1 });
        }, true);
    });

    document.addEventListener('click', function (event) {
        if (!festivalStatusFilter.contains(event.target)) {
            festivalStatusFilter.open = false;
        }
    });
}

const festivalProgressFilter = document.querySelector('#festival-progress-filter');
if (festivalProgressFilter) {
    const festivalProgressButtons = festivalProgressFilter.querySelectorAll('[data-progress-filter]');
    festivalProgressButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            selectedFestivalProgress = button.dataset.progressFilter ?? '';
            festivalProgressButtons.forEach(function (item) {
                item.setAttribute('aria-pressed', String(item === button));
                item.classList.toggle('active', item === button);
            });

            festivalProgressFilter.open = false;
            const summary = festivalProgressFilter.querySelector('summary');
            if (summary) summary.focus();

            changeFestivalQuery({ page: 1 });
        }, true);
    });

    document.addEventListener('click', function (event) {
        if (!festivalProgressFilter.contains(event.target)) {
            festivalProgressFilter.open = false;
        }
    });

    festivalProgressFilter.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            festivalProgressFilter.open = false;
            const summary = festivalProgressFilter.querySelector('summary');
            if (summary) summary.focus();
        }
    });
}

function renderFestivals(festivals) {
    const tbody = document.querySelector('#festival-list');
    const emptyRow = document.querySelector('#festival-empty');
    if (!tbody) return;

    const fragment = document.createDocumentFragment();

    festivals.forEach(function (festival) {
        const progressMeta = getProgressMeta(festival);

        const row = document.createElement('tr');
        row.dataset.status = festival.status;
        row.dataset.progress = progressMeta.key;

        const checkTd = document.createElement('td');
        checkTd.className = 'list-selection-column';
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'form-check-input list-selection-input';
        checkbox.name = 'festivalIds';
        checkbox.value = festival.festivalNumber;
        checkbox.setAttribute('aria-label', '축제 선택');
        checkTd.append(checkbox);

        const idTd = document.createElement('td');
        idTd.dataset.field = 'festivalId';
        idTd.textContent = festival.festivalNumber;

        const statusTd = document.createElement('td');
        const statusBadge = document.createElement('span');
        const isVisible = Number(festival.status) !== 0;
        statusBadge.className = `badge ${isVisible ? 'status-visible' : 'status-hidden'}`;
        statusBadge.textContent = isVisible ? '공개' : '비공개';
        statusTd.append(statusBadge);

        const genreTd = document.createElement('td');
        genreTd.dataset.field = 'genre';
        const genreBadge = document.createElement('span');
        genreBadge.className = 'festival-genre-tag';
        genreBadge.textContent = festival.mainTag ?? '';
        genreBadge.title = festival.mainTag ?? '';
        applyGenreBadgeStyle(genreBadge, resolveFestivalTagColor(festival));
        genreTd.append(genreBadge);

        const titleTd = document.createElement('td');
        titleTd.className = 'festival-title fw-medium text-start';
        titleTd.dataset.field = 'title';
        const titleLink = document.createElement('a');
        titleLink.className = 'list-detail-link';
        titleLink.href = `/admin/festivalDetailView/${encodeURIComponent(festival.festivalNumber)}`;
        titleLink.textContent = festival.festivalName ?? '';
        titleTd.append(titleLink);

        const scheduleTd = document.createElement('td');
        scheduleTd.dataset.sortField = 'startDate';
        scheduleTd.dataset.sortValue = festival.startDate ?? '';
        const startTime = document.createElement('time');
        startTime.dateTime = festival.startDate ?? '';
        startTime.textContent = festival.startDate ?? '';
        const endTime = document.createElement('time');
        endTime.dateTime = festival.endDate ?? '';
        endTime.textContent = festival.endDate ?? '';
        scheduleTd.append(startTime, ' ~ ', endTime);

        const progressTd = document.createElement('td');
        progressTd.className = 'festival-progress-column';
        const progressBadge = document.createElement('span');
        progressBadge.dataset.festivalProgress = '';
        progressBadge.className = `badge ${progressMeta.badgeClass}`;
        progressBadge.textContent = progressMeta.label;
        progressTd.append(progressBadge);

        const placeTd = document.createElement('td');
        placeTd.className = 'festival-location';
        placeTd.dataset.field = 'place';
        placeTd.textContent = festival.address ?? '';

        const likesTd = document.createElement('td');
        likesTd.dataset.sortField = 'likeCount';
        likesTd.textContent = festival.festivalLikes ?? 0;

        row.append(checkTd, idTd, statusTd, genreTd, titleTd, scheduleTd, progressTd, placeTd, likesTd);
        fragment.append(row);
    });

    if (emptyRow) {
        emptyRow.hidden = festivals.length > 0;
        fragment.append(emptyRow);
    }

    tbody.replaceChildren(fragment);
    syncFestivalSelectAllState();
    shortenListText('.festival-title', 30);
    shortenListText('.festival-location', 15);
}

function syncPagination(currentPage, pageCount, totalCount) {
    if (typeof renderPagination !== 'function') return;

    renderPagination({
        containerSelector: '#festival-pagination',
        pagesSelector: '#festival-pagination .pagination',
        currentPage: Number(currentPage) || 1,
        pageCount: Number(pageCount) || 0,
        totalCount: Number(totalCount) || 0,
        onPageChange: function (page) {
            return changeFestivalQuery({ page });
        }
    });
}

async function loadFestivals(params) {
    const response = await fetch(`/admin/festivals?${params.toString()}`);
    if (!response.ok) {
        throw new Error('축제 목록 조회에 실패했습니다.');
    }

    const result = await response.json();
    const list = result.list || [];

    renderFestivals(list);
    if (festivalList && typeof festivalList.resetSelection === 'function') {
        festivalList.resetSelection();
    }
    syncFestivalSelectAllState();

    const countEl = document.querySelector('#festival-result-count');
    if (countEl) {
        countEl.textContent = `검색 결과 ${result.total ?? list.length}건`;
    }

    syncPagination(result.pageNum, result.pages, result.total);

    const viewUrl = new URL(window.location.href);
    viewUrl.search = params.toString();
    history.pushState(null, '', viewUrl);
}

const festivalBulkButton = document.querySelector('button[data-bulk-action="festival"]');
const festivalBulkSelect = document.querySelector('#festival-bulk-status');

if (festivalBulkButton) {
    festivalBulkButton.addEventListener('click', async function () {
        const selectedNumbers = Array.from(
            document.querySelectorAll('#festival-list .list-selection-input:checked')
        ).map(function (checkbox) {
            return Number(checkbox.value);
        }).filter(function (num) {
            return !Number.isNaN(num);
        });

        const selectedStatus = festivalBulkSelect ? festivalBulkSelect.value : '';

        if (selectedNumbers.length === 0) {
            alert('상태를 변경할 축제를 선택해 주세요.');
            return;
        }

        if (selectedStatus === '') {
            alert('변경할 상태를 선택해 주세요.');
            return;
        }

        const updateUrl = festivalBulkButton.dataset.updateUrl || '/admin/festivals/status';
        const params = new URLSearchParams();
        selectedNumbers.forEach(function (number) {
            params.append('festivalNumbers', number);
        });
        params.set('status', Number(selectedStatus));

        try {
            const response = await fetch(`${updateUrl}?${params.toString()}`, {
                method: 'PUT'
            });

            if (!response.ok) {
                throw new Error('축제 상태 변경에 실패했습니다.');
            }

            if (festivalBulkSelect) {
                festivalBulkSelect.value = '';
            }
            if (festivalSelectAll) {
                festivalSelectAll.checked = false;
                festivalSelectAll.indeterminate = false;
            }

            await loadFestivals(new URLSearchParams(window.location.search));
        } catch (error) {
            console.error(error);
            const countEl = document.querySelector('#festival-result-count');
            if (countEl) {
                countEl.textContent = error.message || '상태 변경에 실패했습니다.';
            }
            alert('상태 변경에 실패했습니다.');
        }
    });
}

applyInitialProgressAndGenre();
syncFestivalSelectAllState();

const paginationEl = document.querySelector('#festival-pagination');
if (paginationEl) {
    syncPagination(
        paginationEl.dataset.currentPage,
        paginationEl.dataset.totalPages,
        paginationEl.dataset.totalCount
    );
}
