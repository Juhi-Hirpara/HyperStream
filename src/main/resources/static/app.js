const state = {
    movies: [],
    users: [],
    subscriptions: [],
    watchlist: [],
    paging: {
        movies: { page: 1, pageSize: 30 },
        users: { page: 1, pageSize: 30 },
        subscriptions: { page: 1, pageSize: 30 },
        watchlist: { page: 1, pageSize: 30 }
    }
};

const imageLibrary = {
    action: "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?auto=format&fit=crop&w=1200&q=80",
    drama: "https://images.unsplash.com/photo-1513106580091-1d82408b8cd6?auto=format&fit=crop&w=1200&q=80",
    comedy: "https://images.unsplash.com/photo-1487180144351-b8472da7d491?auto=format&fit=crop&w=1200&q=80",
    thriller: "https://images.unsplash.com/photo-1505685296765-3a2736de412f?auto=format&fit=crop&w=1200&q=80",
    documentary: "https://images.unsplash.com/photo-1478720568477-152d9b164e26?auto=format&fit=crop&w=1200&q=80",
    sciFi: "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?auto=format&fit=crop&w=1200&q=80",
    romance: "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?auto=format&fit=crop&w=1200&q=80",
    fantasy: "https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=1200&q=80",
    default: "https://images.unsplash.com/photo-1518929458119-e5bf444c30f4?auto=format&fit=crop&w=1200&q=80"
};

document.addEventListener("DOMContentLoaded", () => {
    activateNav();
    initPage();
});

function initPage() {
    const page = document.body.dataset.page;
    if (page === "home") initHomePage();
    if (page === "movies") initMoviesPage();
    if (page === "users") initUsersPage();
    if (page === "subscriptions") initSubscriptionsPage();
    if (page === "watchlist") initWatchlistPage();
}

function activateNav() {
    const current = document.body.dataset.page;
    document.querySelectorAll("[data-nav]").forEach((link) => {
        link.classList.toggle("active-link", link.dataset.nav === current);
    });
}

async function apiFetch(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Request failed for ${url}`);
    }

    const contentType = response.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
        return response.json();
    }
    return response.text();
}

async function loadCoreData() {
    const [movies, users, subscriptions] = await Promise.all([
        apiFetch("/api/movies"),
        apiFetch("/api/users"),
        apiFetch("/api/subscriptions")
    ]);
    state.movies = Array.isArray(movies) ? movies : [];
    state.users = Array.isArray(users) ? users : [];
    state.subscriptions = Array.isArray(subscriptions) ? subscriptions : [];
}

function initHomePage() {
    document.getElementById("refresh-all")?.addEventListener("click", loadHomeData);
    loadHomeData();
}

async function loadHomeData() {
    try {
        setStatus("Loading dashboard data...");
        await loadCoreData();
        const defaultUserId = state.users[0]?.id;
        state.watchlist = defaultUserId ? await apiFetch(`/api/watchlist/${defaultUserId}`) : [];
        setText("movie-count", state.movies.length);
        setText("user-count", state.users.length);
        setText("subscription-count", state.subscriptions.length);
        setText("watchlist-count", state.watchlist.length);
        renderHomeHighlights();
        renderHomeRows();
        setStatus("Dashboard ready.", "success");
    } catch (error) {
        setStatus(error.message || "Unable to load dashboard.", "error");
    }
}

function renderHomeHighlights() {
    const genres = [...new Set(state.movies.map((movie) => movie.genre).filter(Boolean))].slice(0, 6);
    const plans = state.subscriptions.slice(0, 6).map((plan) => `${plan.planName || "Plan"} ${plan.active ? "Active" : "Paused"}`);
    const genrePills = document.getElementById("genre-pills");
    const planPills = document.getElementById("plan-pills");
    if (genrePills) genrePills.innerHTML = genres.length ? genres.map((genre) => `<span class="pill">${escapeHtml(genre)}</span>`).join("") : `<span class="pill">No genres yet</span>`;
    if (planPills) planPills.innerHTML = plans.length ? plans.map((plan) => `<span class="pill">${escapeHtml(plan)}</span>`).join("") : `<span class="pill">No plans yet</span>`;
}

function renderHomeRows() {
    const featuredRow = document.getElementById("featured-row");
    const userRow = document.getElementById("user-row");
    const planRow = document.getElementById("plan-row");
    if (featuredRow) featuredRow.innerHTML = state.movies.slice(0, 6).map(createMovieCard).join("") || emptyState("No movies available.");
    if (userRow) userRow.innerHTML = state.users.slice(0, 4).map(createUserCard).join("") || emptyState("No users available.");
    if (planRow) planRow.innerHTML = state.subscriptions.slice(0, 4).map(createSubscriptionCard).join("") || emptyState("No plans available.");
}

function initMoviesPage() {
    document.getElementById("movie-form")?.addEventListener("submit", saveMovie);
    document.getElementById("reset-movie-form")?.addEventListener("click", resetMovieForm);
    document.getElementById("movie-search")?.addEventListener("input", () => resetAndRender("movies", renderMovies));
    document.getElementById("movie-page-size")?.addEventListener("change", (event) => updatePageSize("movies", event.target.value, renderMovies));
    resetMovieForm();
    loadMovies();
}

async function loadMovies() {
    try {
        setStatus("Loading movies...");
        state.movies = await apiFetch("/api/movies");
        renderMovies();
        setText("movie-page-count", state.movies.length);
        setStatus("Movies page ready.", "success");
    } catch (error) {
        setStatus(error.message || "Unable to load movies.", "error");
    }
}

function renderMovies() {
    const search = (document.getElementById("movie-search")?.value || "").trim().toLowerCase();
    const grid = document.getElementById("movies-grid");
    if (!grid) return;
    const filtered = state.movies.filter((movie) => !search || [movie.title, movie.genre, movie.description].filter(Boolean).some((value) => value.toLowerCase().includes(search)));
    const paged = paginateItems("movies", filtered);
    grid.innerHTML = paged.items.length ? paged.items.map(createMovieCard).join("") : emptyState("No movies found.");
    renderPagination("movies", paged, renderMovies);
    document.querySelectorAll("[data-action='edit-movie']").forEach((button) => {
        button.onclick = () => populateMovieForm(Number(button.dataset.id));
    });
    document.querySelectorAll("[data-action='delete-movie']").forEach((button) => {
        button.onclick = () => deleteMovie(Number(button.dataset.id));
    });
}

async function saveMovie(event) {
    event.preventDefault();
    const id = document.getElementById("movie-id").value;
    const payload = {
        title: document.getElementById("movie-title").value.trim(),
        description: document.getElementById("movie-description").value.trim(),
        genre: document.getElementById("movie-genre").value.trim(),
        releaseYear: Number(document.getElementById("movie-release-year").value),
        duration: Number(document.getElementById("movie-duration").value),
        rating: Number(document.getElementById("movie-rating").value)
    };
    try {
        setStatus(id ? "Updating movie..." : "Creating movie...");
        await apiFetch(id ? `/api/movies/${id}` : "/api/movies", { method: id ? "PUT" : "POST", body: JSON.stringify(payload) });
        resetMovieForm();
        await loadMovies();
    } catch (error) {
        setStatus(error.message || "Movie save failed.", "error");
    }
}

function populateMovieForm(id) {
    const movie = state.movies.find((item) => item.id === id);
    if (!movie) return;
    setValue("movie-id", movie.id);
    setValue("movie-title", movie.title || "");
    setValue("movie-description", movie.description || "");
    setValue("movie-genre", movie.genre || "");
    setValue("movie-release-year", movie.releaseYear || "");
    setValue("movie-duration", movie.duration || "");
    setValue("movie-rating", movie.rating || "");
    setStatus(`Editing movie "${movie.title}".`);
    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function deleteMovie(id) {
    try {
        setStatus("Deleting movie...");
        await apiFetch(`/api/movies/${id}`, { method: "DELETE" });
        await loadMovies();
    } catch (error) {
        setStatus(error.message || "Movie delete failed.", "error");
    }
}

function resetMovieForm() {
    document.getElementById("movie-form")?.reset();
    setValue("movie-id", "");
    setValue("movie-rating", "8.5");
}

function initUsersPage() {
    document.getElementById("user-form")?.addEventListener("submit", saveUser);
    document.getElementById("reset-user-form")?.addEventListener("click", resetUserForm);
    document.getElementById("user-search")?.addEventListener("input", () => resetAndRender("users", renderUsers));
    document.getElementById("user-page-size")?.addEventListener("change", (event) => updatePageSize("users", event.target.value, renderUsers));
    resetUserForm();
    loadUsers();
}

async function loadUsers() {
    try {
        setStatus("Loading users...");
        state.users = await apiFetch("/api/users");
        renderUsers();
        setText("user-page-count", state.users.length);
        setStatus("Users page ready.", "success");
    } catch (error) {
        setStatus(error.message || "Unable to load users.", "error");
    }
}

function renderUsers() {
    const search = (document.getElementById("user-search")?.value || "").trim().toLowerCase();
    const grid = document.getElementById("users-grid");
    if (!grid) return;
    const filtered = state.users.filter((user) => !search || [user.name, user.email, user.subscriptionType].filter(Boolean).some((value) => value.toLowerCase().includes(search)));
    const paged = paginateItems("users", filtered);
    grid.innerHTML = paged.items.length ? paged.items.map(createUserCard).join("") : emptyState("No users found.");
    renderPagination("users", paged, renderUsers);
    document.querySelectorAll("[data-action='edit-user']").forEach((button) => {
        button.onclick = () => populateUserForm(Number(button.dataset.id));
    });
    document.querySelectorAll("[data-action='delete-user']").forEach((button) => {
        button.onclick = () => deleteUser(Number(button.dataset.id));
    });
}

async function saveUser(event) {
    event.preventDefault();
    const id = document.getElementById("user-id").value;
    const payload = {
        name: document.getElementById("user-name").value.trim(),
        email: document.getElementById("user-email").value.trim(),
        subscriptionType: document.getElementById("user-subscription-type").value
    };
    try {
        setStatus(id ? "Updating user..." : "Creating user...");
        await apiFetch(id ? `/api/users/${id}` : "/api/users", { method: id ? "PUT" : "POST", body: JSON.stringify(payload) });
        resetUserForm();
        await loadUsers();
    } catch (error) {
        setStatus(error.message || "User save failed.", "error");
    }
}

function populateUserForm(id) {
    const user = state.users.find((item) => item.id === id);
    if (!user) return;
    setValue("user-id", user.id);
    setValue("user-name", user.name || "");
    setValue("user-email", user.email || "");
    setValue("user-subscription-type", user.subscriptionType || "BASIC");
    setStatus(`Editing user "${user.name}".`);
    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function deleteUser(id) {
    try {
        setStatus("Deleting user...");
        await apiFetch(`/api/users/${id}`, { method: "DELETE" });
        await loadUsers();
    } catch (error) {
        setStatus(error.message || "User delete failed.", "error");
    }
}

function resetUserForm() {
    document.getElementById("user-form")?.reset();
    setValue("user-id", "");
    setValue("user-subscription-type", "BASIC");
}

function initSubscriptionsPage() {
    document.getElementById("subscription-form")?.addEventListener("submit", saveSubscription);
    document.getElementById("reset-subscription-form")?.addEventListener("click", resetSubscriptionForm);
    document.getElementById("subscription-page-size")?.addEventListener("change", (event) => updatePageSize("subscriptions", event.target.value, loadSubscriptions));
    resetSubscriptionForm();
    loadSubscriptions();
}

async function loadSubscriptions() {
    try {
        setStatus("Loading subscriptions...");
        state.subscriptions = await apiFetch("/api/subscriptions");
        const grid = document.getElementById("subscriptions-grid");
        if (grid) {
            const paged = paginateItems("subscriptions", state.subscriptions);
            grid.innerHTML = paged.items.length ? paged.items.map(createSubscriptionCard).join("") : emptyState("No subscription plans available.");
            renderPagination("subscriptions", paged, loadSubscriptions);
        }
        setText("subscription-page-count", state.subscriptions.length);
        document.querySelectorAll("[data-action='delete-subscription']").forEach((button) => {
            button.onclick = () => deleteSubscription(Number(button.dataset.id));
        });
        setStatus("Subscriptions page ready.", "success");
    } catch (error) {
        setStatus(error.message || "Unable to load subscriptions.", "error");
    }
}

async function saveSubscription(event) {
    event.preventDefault();
    const payload = {
        planName: document.getElementById("subscription-plan-name").value.trim(),
        price: Number(document.getElementById("subscription-price").value),
        durationInDays: Number(document.getElementById("subscription-duration").value),
        active: document.getElementById("subscription-active").checked
    };
    try {
        setStatus("Creating subscription plan...");
        await apiFetch("/api/subscriptions", { method: "POST", body: JSON.stringify(payload) });
        resetSubscriptionForm();
        await loadSubscriptions();
    } catch (error) {
        setStatus(error.message || "Subscription save failed.", "error");
    }
}

async function deleteSubscription(id) {
    try {
        setStatus("Deleting subscription...");
        await apiFetch(`/api/subscriptions/${id}`, { method: "DELETE" });
        await loadSubscriptions();
    } catch (error) {
        setStatus(error.message || "Subscription delete failed.", "error");
    }
}

function resetSubscriptionForm() {
    document.getElementById("subscription-form")?.reset();
    const active = document.getElementById("subscription-active");
    if (active) active.checked = true;
}

function initWatchlistPage() {
    document.getElementById("watchlist-form")?.addEventListener("submit", addWatchlistItem);
    document.getElementById("load-watchlist")?.addEventListener("click", loadWatchlistForSelectedUser);
    document.getElementById("watchlist-page-size")?.addEventListener("change", (event) => updatePageSize("watchlist", event.target.value, loadWatchlistForSelectedUser));
    loadWatchlistCore();
}

async function loadWatchlistCore() {
    try {
        setStatus("Loading watchlist workspace...");
        await loadCoreData();
        populateWatchlistSelects();
        if (state.users[0]) {
            setValue("watchlist-user-id", String(state.users[0].id));
            await loadWatchlistForSelectedUser();
        } else {
            renderWatchlist();
        }
        setStatus("Watchlist page ready.", "success");
    } catch (error) {
        setStatus(error.message || "Unable to load watchlist workspace.", "error");
    }
}

function populateWatchlistSelects() {
    const userSelect = document.getElementById("watchlist-user-id");
    const movieSelect = document.getElementById("watchlist-movie-id");
    if (userSelect) {
        userSelect.innerHTML = state.users.length ? state.users.map((user) => `<option value="${user.id}">${escapeHtml(user.name)} (${escapeHtml(user.subscriptionType || "N/A")})</option>`).join("") : `<option value="">No users available</option>`;
    }
    if (movieSelect) {
        movieSelect.innerHTML = state.movies.length ? state.movies.map((movie) => `<option value="${movie.id}">${escapeHtml(movie.title)}</option>`).join("") : `<option value="">No movies available</option>`;
    }
}

async function addWatchlistItem(event) {
    event.preventDefault();
    const userId = document.getElementById("watchlist-user-id")?.value;
    const movieId = document.getElementById("watchlist-movie-id")?.value;
    if (!userId || !movieId) {
        setStatus("Select both a user and movie before adding to watchlist.", "error");
        return;
    }
    try {
        setStatus("Adding item to watchlist...");
        await apiFetch(`/api/watchlist?userId=${encodeURIComponent(userId)}&movieId=${encodeURIComponent(movieId)}`, { method: "POST" });
        await loadWatchlistForSelectedUser();
    } catch (error) {
        setStatus(error.message || "Failed to add watchlist item.", "error");
    }
}

async function loadWatchlistForSelectedUser() {
    const userId = document.getElementById("watchlist-user-id")?.value;
    if (!userId) {
        state.watchlist = [];
        renderWatchlist();
        return;
    }
    try {
        setStatus("Loading watchlist...");
        state.watchlist = await apiFetch(`/api/watchlist/${userId}`);
        const selectedUser = state.users.find((user) => String(user.id) === String(userId));
        setText("watchlist-heading", `${selectedUser?.name || "Selected user"} watchlist`);
        setText("watchlist-page-count", state.watchlist.length);
        renderWatchlist();
        setStatus("Watchlist loaded.", "success");
    } catch (error) {
        state.watchlist = [];
        renderWatchlist();
        setStatus(error.message || "Unable to load watchlist.", "error");
    }
}

function renderWatchlist() {
    const grid = document.getElementById("watchlist-grid");
    if (!grid) return;
    const paged = paginateItems("watchlist", state.watchlist);
    grid.innerHTML = paged.items.length ? paged.items.map(createWatchlistCard).join("") : emptyState("This user has no watchlist items yet.");
    renderPagination("watchlist", paged, loadWatchlistForSelectedUser);
    document.querySelectorAll("[data-action='remove-watchlist']").forEach((button) => {
        button.onclick = () => removeWatchlistItem(Number(button.dataset.id));
    });
}

async function removeWatchlistItem(id) {
    try {
        setStatus("Removing watchlist item...");
        await apiFetch(`/api/watchlist/${id}`, { method: "DELETE" });
        await loadWatchlistForSelectedUser();
    } catch (error) {
        setStatus(error.message || "Watchlist remove failed.", "error");
    }
}

function createMovieCard(movie) {
    const imageUrl = resolveImage(movie.genre);
    return `
        <article class="movie-card" style="--card-image: url('${imageUrl}')">
            <div class="movie-card-content">
                <div class="meta-row">
                    <span class="badge">${escapeHtml(movie.genre || "Featured")}</span>
                    <span>${escapeHtml(String(movie.releaseYear || "N/A"))}</span>
                </div>
                <h3>${escapeHtml(movie.title || "Untitled")}</h3>
                <p>${escapeHtml(movie.description || "No description available.")}</p>
                <div class="meta-row">
                    <span>${escapeHtml(String(movie.duration || 0))} min</span>
                    <span>Rating ${escapeHtml(String(movie.rating ?? "N/A"))}</span>
                </div>
                <div class="action-row">
                    <button class="action-btn" type="button" data-action="edit-movie" data-id="${movie.id}">Edit</button>
                    <button class="action-btn delete" type="button" data-action="delete-movie" data-id="${movie.id}">Delete</button>
                </div>
            </div>
        </article>
    `;
}

function createUserCard(user) {
    const initials = (user.name || "U").split(" ").map((part) => part[0]).join("").slice(0, 2).toUpperCase();
    return `
        <article class="user-card">
            <div class="user-card-header">
                <div class="user-avatar">${escapeHtml(initials)}</div>
                <span class="badge">${escapeHtml(user.subscriptionType || "N/A")}</span>
            </div>
            <h3>${escapeHtml(user.name || "Unknown User")}</h3>
            <p>${escapeHtml(user.email || "No email available")}</p>
            <p class="subtle-text">Created: ${escapeHtml(formatDate(user.createdAt))}</p>
            <div class="action-row">
                <button class="action-btn" type="button" data-action="edit-user" data-id="${user.id}">Edit</button>
                <button class="action-btn delete" type="button" data-action="delete-user" data-id="${user.id}">Delete</button>
            </div>
        </article>
    `;
}

function createSubscriptionCard(plan) {
    return `
        <article class="plan-card ${plan.active ? "active" : ""}">
            <div class="plan-price-row">
                <h3>${escapeHtml(plan.planName || "Plan")}</h3>
                <span class="badge">${plan.active ? "Active" : "Inactive"}</span>
            </div>
            <p class="plan-price">Rs ${escapeHtml(Number(plan.price || 0).toFixed(2))}</p>
            <p>${escapeHtml(String(plan.durationInDays || 0))} days of streaming access</p>
            <p class="plan-status subtle-text">This section matches the backend create/get/delete support.</p>
            <div class="action-row">
                <button class="action-btn delete" type="button" data-action="delete-subscription" data-id="${plan.id}">Delete</button>
            </div>
        </article>
    `;
}

function createWatchlistCard(item) {
    const movie = item.movie || {};
    const user = item.user || {};
    const imageUrl = resolveImage(movie.genre);
    return `
        <article class="watchlist-card" style="--card-image: url('${imageUrl}')">
            <div class="watchlist-card-content">
                <span class="badge">${escapeHtml(user.name || "Viewer")}</span>
                <h3>${escapeHtml(movie.title || "Untitled")}</h3>
                <p>${escapeHtml(movie.description || "No description available.")}</p>
                <div class="meta-row">
                    <span>${escapeHtml(movie.genre || "Genre N/A")}</span>
                    <span>${escapeHtml(formatDate(item.addedAt))}</span>
                </div>
                <div class="action-row">
                    <button class="action-btn delete" type="button" data-action="remove-watchlist" data-id="${item.id}">Remove</button>
                </div>
            </div>
        </article>
    `;
}

function resolveImage(genre) {
    const normalized = (genre || "").toLowerCase();
    if (normalized.includes("action")) return imageLibrary.action;
    if (normalized.includes("drama")) return imageLibrary.drama;
    if (normalized.includes("comedy")) return imageLibrary.comedy;
    if (normalized.includes("thriller")) return imageLibrary.thriller;
    if (normalized.includes("documentary")) return imageLibrary.documentary;
    if (normalized.includes("sci") || normalized.includes("space")) return imageLibrary.sciFi;
    if (normalized.includes("romance")) return imageLibrary.romance;
    if (normalized.includes("fantasy")) return imageLibrary.fantasy;
    return imageLibrary.default;
}

function setStatus(message, tone = "") {
    const statusBanner = document.getElementById("status-banner");
    if (!statusBanner) return;
    statusBanner.textContent = message;
    statusBanner.className = `status-banner ${tone}`.trim();
}

function formatDate(value) {
    if (!value) return "N/A";
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return value;
    return new Intl.DateTimeFormat("en-IN", { year: "numeric", month: "short", day: "numeric" }).format(date);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

function emptyState(message) {
    return `<div class="empty-state">${escapeHtml(message)}</div>`;
}

function setText(id, value) {
    const element = document.getElementById(id);
    if (element) element.textContent = value;
}

function setValue(id, value) {
    const element = document.getElementById(id);
    if (element) element.value = value;
}

function paginateItems(key, items) {
    const paging = state.paging[key];
    const totalItems = items.length;
    const totalPages = Math.max(1, Math.ceil(totalItems / paging.pageSize));
    paging.page = Math.min(paging.page, totalPages);
    const start = (paging.page - 1) * paging.pageSize;
    const end = start + paging.pageSize;
    return {
        items: items.slice(start, end),
        totalItems,
        totalPages,
        page: paging.page,
        pageSize: paging.pageSize,
        start: totalItems === 0 ? 0 : start + 1,
        end: Math.min(end, totalItems)
    };
}

function renderPagination(key, paged, renderFn) {
    const container = document.getElementById(`${key}-pagination`);
    if (!container) {
        return;
    }

    if (paged.totalItems === 0) {
        container.innerHTML = "";
        return;
    }

    container.innerHTML = `
        <div class="pagination-bar">
            <div class="pagination-summary">Showing ${paged.start}-${paged.end} of ${paged.totalItems}</div>
            <div class="pagination-actions">
                <button class="page-btn" type="button" data-page-action="prev" ${paged.page <= 1 ? "disabled" : ""}>Prev</button>
                <span class="page-indicator">Page ${paged.page} / ${paged.totalPages}</span>
                <button class="page-btn" type="button" data-page-action="next" ${paged.page >= paged.totalPages ? "disabled" : ""}>Next</button>
            </div>
        </div>
    `;

    container.querySelector("[data-page-action='prev']")?.addEventListener("click", () => {
        changePage(key, -1, renderFn);
    });
    container.querySelector("[data-page-action='next']")?.addEventListener("click", () => {
        changePage(key, 1, renderFn);
    });
}

function changePage(key, delta, renderFn) {
    state.paging[key].page = Math.max(1, state.paging[key].page + delta);
    renderFn();
    window.scrollTo({ top: 0, behavior: "smooth" });
}

function updatePageSize(key, value, renderFn) {
    state.paging[key].pageSize = Number(value);
    state.paging[key].page = 1;
    renderFn();
}

function resetAndRender(key, renderFn) {
    state.paging[key].page = 1;
    renderFn();
}
