async function getFavoriteItems() {
    const response = await fetch("/api/favorites/items")
    const data = await response.json();
    if(data.length > 0) {
        checkCartButton()
    }
    if(document.getElementById("favoriteItems")) {
        data.forEach(item=> insertFavoriteItemRow(item))
    }

}
async function getFavoriteShops() {
    const response = await fetch("/api/favorites/shops")
    const data = await response.json();
    if(document.getElementById("favoriteShops")) {
        data.forEach(shop=> insertFavoriteShopRow(shop))
    }
}
getFavoriteItems();
getFavoriteShops();

function checkCartButton() {
    let cartDiv = document.getElementById("cartFromFavoritesButton");
    cartDiv.innerHTML =
        `<button class="btn btn-success btn-block" onclick="addToCartFromFavorites()">Добавить в корзину</button>`;
}

function insertFavoriteItemRow(item){
    let i = {
        id: item.id,
        name: item.name,
        price: item.price,
        count: item.count,
        categories: item.categories,
        images: item.images,
        description: item.description,
        shopId: item.shopId
    };
    document.querySelector('#favoriteItems').insertAdjacentHTML('beforeend', `
    <div class="container" id="favoriteItem${i.id}" name="favoriteItemRow">
        <div class="row">
            <h5>${i.name}</h5>
        </div>
        <div class="row">
            <div class="col-1">
                <input type="checkbox" id="checkBox${i.id}&${i.shopId}" name="checkBox"> 
            </div>
            <div class="col-3">
              <img src="${i.images[0]}" class="img-fluid"/>
            </div>
            <div class="col-3">
              <p>Цена:</p>
              <p>${i.price}</p>
            </div>
            <div class="col-4">
                <p>${i.description}</p>
            </div>
            <div class="col-1">
                <a href="#" onclick="collapseFavoriteItem(${i.id}); deleteFavoriteItem(${i.id})"><i class="fas fa-minus-circle" style="color: red" onclick=""></i></a>
            </div>
        </div>
        <div class="row m-1">&nbsp;</div>
    </div>
    `)

}
function insertFavoriteShopRow(shop) {
    let s = {
        id: shop.id,
        name: shop.name,
        logo: shop.logo,
        description: shop.description,
    };
    document.querySelector('#favoriteShops').insertAdjacentHTML('beforeend', `
    <div class="container" id="favoriteShop${s.id}" name="favoriteShopRow">
        <div class="row">
            <h5>${s.name}</h5>
        </div>
        <div class="row">
            <div class="col-4">
              <img src="${s.logo}" class="img-fluid"/>
            </div>
            <div class="col-7">
                <p>${s.description}</p>
            </div>
            <div class="col-1">
                <a href="#" onclick="collapseFavoriteShop(${s.id}); deleteFavoriteShop(${s.id})"><i class="fas fa-minus-circle" style="color: red" onclick=""></i></a>
            </div>
        </div>
        <div class="row m-1">&nbsp;</div>
    </div>
    `)
}

function collapseFavoriteShop(id){
    let idHTML = "favoriteShop"+id;
    document.getElementById(idHTML).remove();
}
async function deleteFavoriteShop(id){
    let url = new URL("http://localhost:8888/api/favorites/shops/delete/"+id);

    const response = await fetch(url, {
        headers: { "Content-Type": "application/json; charset=utf-8" },
        method: 'DELETE'
    })
}
function collapseFavoriteItem(id){
    let idHTML = "favoriteItem"+id;
    document.getElementById(idHTML).remove();
}
async function deleteFavoriteItem(id){
    let url = new URL("http://localhost:8888/api/favorites/items/delete/"+id);

    const response = await fetch(url, {
        headers: { "Content-Type": "application/json; charset=utf-8" },
        method: 'DELETE'
    })
}

function addToCartFromFavorites() {
    let checkBoxes = document.getElementsByName("checkBox");
    console.log(checkBoxes, checkBoxes.length)
    for (let i = 0; i < checkBoxes.length; i++) {
        if(checkBoxes[i].checked) {
            let idCheckbox = checkBoxes[i].id;
            let itemId = idCheckbox.substr(idCheckbox.indexOf("x")+1, idCheckbox.indexOf("&")-(idCheckbox.indexOf("x")+1));
            let shopdId = idCheckbox.substr(idCheckbox.indexOf("&")+1, idCheckbox.length - idCheckbox.indexOf("&")+1);
            console.log(itemId, shopdId)
            addSpecificItemFromFavorites(itemId, shopdId)
        }
    }
}

async function addSpecificItemFromFavorites(itemId, shopId) {

    let url = new URL("http://localhost:8888/api/cart/add")
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify({
            quantity: 1,
            item: {
                id: itemId
            },
            shop: {
                id: shopId
            },
        })
    })
}

async function favoriteButtonItem(itemId) {
    let itemExists = await fetch("/api/favorites/existsItem/" + itemId).then(res => res.text());
    let buttonDiv = document.getElementById("favoriteButton");
    let buttonDivId = "favoriteItem" + itemId;
    if (itemExists === 'true') {
        buttonDiv.innerHTML = `<button type="button"  id=${buttonDivId} class="btn btn-info" >В избранное</button>`;
    } else if(itemExists === 'false'){
        buttonDiv.innerHTML = `<button type="button"  id=${buttonDivId} class="btn btn-secondary">В избранное</button>`;
    }
    let functionItem = "#favoriteItem" + itemId;

    $(functionItem).click(function () {
        addItemToFavorites(itemId);
        if ($(this).hasClass('btn btn-info')) {
            deleteFavoriteItem(itemId)
            $(this).removeClass('btn btn-info');
            $(this).addClass('btn btn-secondary');
        } else {
            $(this).removeClass('btn btn-secondary');
            $(this).addClass('btn btn-info');
        }
    });
}


async function addItemToFavorites(itemId) {

    let url = new URL("http://localhost:8888/api/favorites/items/add/"+itemId)
    const response = await fetch(url, {
        headers: { "Content-Type": "application/json; charset=utf-8" },
        method: 'PATCH',
        body: null
    })
}

