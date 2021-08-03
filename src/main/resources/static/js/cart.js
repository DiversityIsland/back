async function getItems() {
    const response = await fetch("/api/cart")
    const data = await response.json();
    console.log(data)
    if(document.getElementById("cartItems")) {
        data.forEach(cartItem=> insertCartItemRow(cartItem))
    }
}
getItems()
let i = 0;
function insertCartItemRow(cartItem) {
    let ci = {
        id: cartItem.id,
        quantity: cartItem.quantity,
        item: cartItem.item,
        shop: cartItem.shop,
        user: cartItem.user
    };
    i++;
    document.querySelector('#cartItems').insertAdjacentHTML('beforeend', `
    <div class="row rounded" id="cartItem${ci.id}" style="border: 1px solid">
        <div class="col-2">
          <div class="mt-2"><h4>${i}</h4></div>
          <div><button type="submit" onclick="deleteCartItem(${ci.id}); collapseRow(${ci.id});" style="border: none"><img src="https://static.thenounproject.com/png/147529-200.png" class="img-fluid"
          width="25" height="25"/></button></div>
        </div>
        <div class="col-3">
          <img src="http://i1.adis.ws/i/canon/eos-r6-rf24-105mm-f4_7.1_is_stm_front-on_square_6412568cc0e7484b96bd55e43069a56c" class="img-fluid" />
        </div>
        <div class="col-7">
          <p id="itemName">${ci.item.name}</p>
          <p id="itemDescription">${ci.item.description}</p>
          <div class="col-3">
            <div>
              <label for="cartItemQuantity">Количество:</label>
              <input type="number" id="cartItemQuantity${ci.id}" value="${ci.quantity}" class="form-control" min="1">
              <button type="submit" class="btn btn-secondary btn-sm mt-2" id="changeQuantity" onclick=
              "sumForCartItem(${ci.id}, ${ci.item.price}); updateQuantity(${ci.id}); subtotalForCartItems();">Пересчитать сумму</button>
            </div>
          </div>
          <div>
            <span>X</span>
            <span id="itemPrice">${ci.item.price}</span>
          </div>
          <div>
            <span>= </span>
            <h5><span id="itemTotalPrice${ci.id}" name="itemTotalPrice"></span></h5>
          </div>
        </div>
    </div> 
    <div class="row m-1">&nbsp;</div>
    `)
    sumForCartItem(ci.id, ci.item.price);
    subtotalForCartItems();
}

async function deleteCartItem(id) {
    let url = new URL("http://localhost:8888/api/cart/delete/"+id)

    const response = await fetch(url, {
        headers: { "Content-Type": "application/json; charset=utf-8" },
        method: 'DELETE'
    })
}

function collapseRow(id) {
    let idHTML = "cartItem"+id;
    document.getElementById(idHTML).remove();
}

async function updateQuantity(id) {

    let quantHTML = "cartItemQuantity"+id;
    let newQuant = document.getElementById(quantHTML).value;

    const response = await fetch("http://localhost:8888/api/cart/update/"+id, {
        headers: { "Content-Type": "application/json; charset=utf-8" },
        method: 'PATCH',
        body: JSON.stringify({
            quantity: newQuant,
        })
    })
}

function sumForCartItem(id, price) {
    let idHTML = "itemTotalPrice"+id;
    let quantHTML = "cartItemQuantity"+id;
    let newQuant = document.getElementById(quantHTML).value;
    let sum = newQuant * price;
    document.getElementById(idHTML).innerHTML = `<span>${sum}</span>`;
}



function subtotalForCartItems() {
    let result = 0;
    let elems = document.getElementsByName("itemTotalPrice");
    for (let j = 0; j < elems.length; j++) {
        let el = elems[j].innerText;
        result = result + parseFloat(el);
    }
    document.getElementById("itemSubtotal").innerHTML = `<span>${result}</span>`;
}