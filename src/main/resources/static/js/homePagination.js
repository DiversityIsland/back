const urlGetLastPageNum = "/api/mainpage/item/lastpage/category/" //  + id/pageSize
const urlGetPageByPageNum = "/api/mainpage/item/category/" // + id/pageNumber/pageSize

const paginationNav = document.querySelector("#pagination_nav")

let activePageLi
let prevPageLi
let nextPageLi

const pageSize = 5
const firstPage = 1
let activePage
let nextPage
let prevPage
let lastPage
let categoryIdGlobalVar

async function firstPaginationLinksShow(categoryId) {
    const response = await fetch(urlGetLastPageNum + categoryId + "/" + pageSize)
    lastPage = await response.json()
    nextPage = activePage + 1
    prevPage = activePage - 1

    paginationNav.innerHTML = `<ul class="pagination" id="pagination_ul">
                                        <li class="page-item disabled" id="prevPageLi">
                                              <a class="page-link" href="#" >Пред.</a>
                                        </li>
                                        <li class="page-item active" aria-current="page" id="activePageLi">
                                              <a class="page-link" href="#">${firstPage}</a>
                                        </li>
                                        <li class="page-item" id="nextPageLi">
                                              <a class="page-link" href="#" onclick="showPage(nextPage)">След.</a>
                                        </li>
                                   </ul>`

    activePageLi = document.querySelector("#activePageLi")
    prevPageLi = document.querySelector("#prevPageLi")
    nextPageLi = document.querySelector("#nextPageLi")

    if (firstPage === lastPage) {
        nextPageLi.classList.add("disabled")
    }
}

async function showPage(pageNum) {
    const response = await fetch(urlGetPageByPageNum + categoryIdGlobalVar + "/" + pageNum + "/" + pageSize)
    const items = await response.json()

    let itemsByCategoryHTML = "";
    for (let i = 0; i < items.length; i++) {
        itemsByCategoryHTML += '<div class="card" style="width: 18rem; margin-right: 15px; margin-top: 20px;">\n' +
            '                       <img src="' + items[i].images[0] + '" class="card-img-top" alt="popular_item" style="height: 180px;">' +
            '                           <div class="card-body">\n' +
            '                                <h5 class="card-title">' + items[i].name + '</h5>' +
            '                                <p class="card-text">' + items[i].description + '</p>' +
            '                                <div class="d-flex">\n' +
            '                                    <a href="#" class="btn btn-danger" style="width: 150px;">В корзину</a>' +
            '                                    <a href="/product/' + items[i].id + '" class="btn btn-primary" style="margin-left: 10px;">Смотреть</a>' +
            '                                </div>' +
            '                           </div>' +
            '                   </div>';
    }
    dinamic_inner_content.html("");
    dinamic_inner_content.html(itemsByCategoryHTML);

    if (pageNum > activePage) {
        activePage++
        nextPage++
        prevPage++

        if (activePage === lastPage) {
            nextPageLi.classList.add("disabled")
        } else {
            nextPageLi.innerHTML = `<a class="page-link" href="#" onclick="showPage(nextPage)">Пред.</a>`
        }
        activePageLi.innerHTML = `<a class="page-link" href="#">${activePage}</a>`
        prevPageLi.innerHTML = `<a class="page-link" href="#" onclick="showPage(prevPage)">Пред.</a>`
        prevPageLi.classList.remove("disabled")
    } else {
        activePage--
        nextPage--
        prevPage--

        if (activePage === firstPage) {
            prevPageLi.classList.add("disabled")
        } else {
            prevPageLi.innerHTML = `<a class="page-link" href="#" onclick="showPage(prevPage)">Пред.</a>`
        }
        activePageLi.innerHTML = `<a class="page-link" href="#">${activePage}</a>`
        nextPageLi.innerHTML = `<a class="page-link" href="#" onclick="showPage(nextPage)">Пред.</a>`
        nextPageLi.classList.remove("disabled")
    }
}