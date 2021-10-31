const urlGetLastPageNumber = "/api/messenger/search/" // + /searchName
const urlGetPageUserBySearchNameAndPageNum = "/api/messenger/search/"  // + /searchName/pageNum
const urlGetUser = "/api/messenger/" // + /id or /principal
const urlGetUsersConnectWithCurrentUser = "/api/messenger/private/chat/user/" // get users connect with current user by id
const urlGetChatToIdAndFromId = "/api/messenger/" // + /toId/fromId
const urlGetMessagesByChatId = "/api/messenger/chat/" // + chatId


const usersList = document.querySelector("#usersList")
const privateChats = document.querySelector("#privateChats")
const chatHistory = document.querySelector("#chat-history")
const chatHistoryElement = document.querySelector("#chatHistoryDiv")
const chatHeaderLogo = document.querySelector("#chatHeaderLogo")
const searchResult = document.querySelector("#searchResult")
const searchName = document.querySelector("#userName")
const paginationPrevious = document.querySelector("#paginPrevious")
const paginationNext = document.querySelector("#paginNext")
const paginationActivePage = document.querySelector("#activePageShow")


let inputTextMessage
let stompClient
let principalObj
let selectedUserId
let selectedUserIdFinalFromSendMsg
let chatId
let toUserName
let icon
let count = 0;
let searchNameValue
let searchNameValueFinal
let lastPageNum
let activePage

// поиск пользователя по имени
async function search() {
    activePage = 1
    const response = await fetch(urlGetPageUserBySearchNameAndPageNum + searchNameValue + "/" + activePage)
    const users = await response.json()

    if (users.length !== 0 ) {
        searchNameValueFinal = searchNameValue
        searchResult.classList.remove('searchR')
        searchResult.innerHTML = "Результаты поиска: \"" + searchNameValue + "\""
        showUsersInHtml(users, usersList)
        getLastPageNum().then(() => {
            paginationLinks()
        })
    } else {
        searchResult.classList.add('searchR')
        searchResult.innerHTML = `Пользователь не найден!`
        usersList.innerHTML = ``
        paginationPrevious.innerHTML = ``
        paginationNext.innerHTML = ``
        paginationActivePage.innerHTML = ``
    }
}
// номер последней страницы пользователей
async function getLastPageNum() {
    const res = await fetch(urlGetLastPageNumber + searchNameValue)
    lastPageNum = await res.json()
}
//след стр
async function showNextPage(pageNum) {
    const response = await fetch(urlGetPageUserBySearchNameAndPageNum + searchNameValueFinal + "/" + pageNum)
    const users = await response.json()

    activePage++
    showUsersInHtml(users, usersList)
    paginationLinks()
}
//пред стр
async function showPreviousPage(pageNum) {
    const response = await fetch(urlGetPageUserBySearchNameAndPageNum + searchNameValueFinal + "/" + pageNum)
    const users = await response.json()

    activePage--
    showUsersInHtml(users, usersList)
    paginationLinks()
}
//показ пагинации
function paginationLinks() {
    let nextPage = activePage + 1
    let previousPage = activePage - 1

    paginationActivePage.innerHTML = `<a href="#" disabled>${activePage}</a>`
    if (previousPage >= 1 ) {
        paginationPrevious.innerHTML = `<a href="#" onclick="showPreviousPage(${previousPage})">❮</a>`
    } else {
        paginationPrevious.innerHTML = ``
    }

    if (nextPage <= lastPageNum) {
        paginationNext.innerHTML = `<a href="#" onclick="showNextPage(${nextPage})">❯</a>`
    } else {
        paginationNext.innerHTML = ``
    }
}
//отправка формы поиска на Enter
searchName.addEventListener("keyup", function (event) {
    searchNameValue = searchName.value
    const searchTrim = searchNameValue.replaceAll(/\s/g, '')
    if (event.keyCode === 13 && searchTrim !== "") {
        search()
    }
})

//список личных сообщений
async function getUsersConnectWithCurrentUser(principalId) {
    const response = await fetch(urlGetUsersConnectWithCurrentUser + principalId)
    const users = await response.json()

    showUsersInHtml(users, privateChats)
}
//данные авторизированного пользователя
async function getPrincipal() {
    const response = await fetch(urlGetUser + "principal")
    const principal = await response.json()

    connect(principal.id)
    principalObj = principal;
}

getPrincipal().then(() => {
    getUsersConnectWithCurrentUser(principalObj.id)
})
//регистрация STOMP и подписка на сообщения авторизированного пользователя
function connect(id) {
    let socket = new SockJS("/chat");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messenger/' + id, function (message) {
            showMessage(JSON.parse(message.body))
        });
    });
}
// показ сообщений если открыт диалог с пользователем который прислал сообщение
async function showMessage(message) {

    if (selectedUserId === message.from) {
        const temp = `<li class="clearfix">
                    <div class="message-data">
                        <span class="message-data-name"> </span>
                    </div>
                    <div class="message my-message">
                        ${message.textMessage}
                    </div>
                 </li>`

        chatHistory.innerHTML += temp;
        scrollHistory()
    }
    if (selectedUserId === message.from && chatId === null) {
        chatId = message.chat
        getUsersConnectWithCurrentUser(principalObj.id)
    }
}

//возвращает чат по ид собеседника
async function getChat(toUserId) {
    const response = await fetch(urlGetChatToIdAndFromId + toUserId + "/" + principalObj.id)
    return await response.json()
}
//возвращает пользователя по ид
async function getUser(userId) {
    const response = await fetch(urlGetUser + userId)
    return await response.json()
}
//возвращает все сообщения в чате по ид чата
async function getMessagesByChatId(Id) {
    const response = await fetch(urlGetMessagesByChatId + Id)
    return await response.json();
}

//при клике на пользователя отображает историю сообщений и т.д.
function selectUser(toUserId) {
    if (toUserId === principalObj.id || toUserId === selectedUserId) {
        return false
    }
    selectedUserId = toUserId;

    getUser(toUserId).then((u) => {
        toUserName = u.firstName + " " + u.lastName
        if (u.gender === "FEMALE"){
            icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135789.png" width="50px" height="50px" alt="avatar"/>`
        } else {
            icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" width="50px" height="50px" alt="avatar"/>`
        }
    }).then(() => {
        chatHeaderLogo.innerHTML = `${icon}
                                    <div class="chat-about">
                                        <div class="chat-with" id="chatWithUser"> </div>
                                    </div>`

    }).then(() => {
        const chatWith = document.querySelector("#chatWithUser")
        chatWith.innerHTML = `<b>${toUserName}</b>`
        if (count === 0) {
            document.querySelector("#inputTextArea").innerHTML = `<textarea id="messageToSend" name="message-to-send" placeholder="Type your message" rows="3"></textarea>
                                                                          <button onclick="wrapperSendMessage()">Send</button>`
            inputTextMessage = document.querySelector("#messageToSend")
            count++
        }
    })

    getChat(toUserId).then((chat) => {
        if (chat.id === null) {
            chatId = null
        } else {
            chatId = chat.id
        }
    }).then(() => {
        if (chatId != null) {
            getMessagesByChatId(chatId).then((messages) => {
                let temp = ""
                messages.forEach((message) => {
                    if (message.from === principalObj.id) {
                        temp += `<li class="clearfix">
                                    <div class="message-data align-right">
                                        <span class="message-data-name"></span></i>
                                    </div>
                                    <div class="message other-message float-right">
                                        ${message.textMessage}
                                    </div>
                                 </li>`
                    } else {
                        temp += `<li>
                                    <div class="message-data">
                                        <span class="message-data-name"> </span>
                                    </div>
                                    <div class="message my-message">
                                        ${message.textMessage}
                                    </div>
                                </li>`
                    }
                    chatHistory.innerHTML = temp;
                    scrollHistory()
                })
            })
        } else {
            chatHistory.innerHTML = ""
        }
    })
}

function scrollHistory() {
    chatHistoryElement.scrollTop = chatHistoryElement.scrollHeight
}

//отправка сообщений в контроллер
async function sendMessage() {
    let inputTrim = inputTextMessage.value
    inputTrim = inputTrim.replaceAll(/\s/g, '')
    if (inputTrim !== "") {
        stompClient.send("/app/chat", {}, JSON.stringify({
            to: selectedUserId,
            from: principalObj.id,
            chat: chatId,
            textMessage: inputTextMessage.value
        }));

        const temp = `<li class="clearfix">
                        <div class="message-data align-right">
                            <span class="message-data-name"></span></i>
                        </div>
                        <div class="message other-message float-right">
                                ${inputTextMessage.value}
                        </div>
                  </li>`

        chatHistory.innerHTML += temp;
        selectedUserIdFinalFromSendMsg = selectedUserId
        scrollHistory()
        inputTextMessage.value = ""
    }
}
//обертка над методом отправки сообщений для решения багов связанных с отправкой первого сообщения
function wrapperSendMessage() {
    sendMessage().then(() => {
        if (chatId === null) {
            setTimeout(() => {
                getChat(selectedUserIdFinalFromSendMsg).then(chat => {
                    chatId = chat.id
                })

                setTimeout(() => {
                    getUser(selectedUserIdFinalFromSendMsg).then(user => {

                        if (user.gender === "FEMALE") {
                            icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135789.png" width="50px" height="50px" alt="avatar"/>`
                        } else {
                            icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" width="50px" height="50px" alt="avatar"/>`
                        }
                        privateChats.innerHTML += `<a href="#" onclick="selectUser(${user.id})">
                                        <li class="clearfix">
                                            ${icon}
                                            <div class="about">
                                            <div class="name"><span id="usersName">${user.firstName}&nbsp;&nbsp;${user.lastName}</span></div>
                                            <div class="status"></div>
                                            </div>
                                        </li>`
                    })
                }, 900)
            }, 800)
        }
    })
}//отображение списка в соответствующем разделе пользователей
function showUsersInHtml(users, htmlTag) {
    let temp = ""
    users.forEach((user) => {

        let me = ""
        if (user.id === principalObj.id) {
            me = `<b>"Вы"</b>`
        }
            if (user.gender === "FEMALE") {
                icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135789.png" width="50px" height="50px" alt="avatar"/>`
            } else {
                icon = `<img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" width="50px" height="50px" alt="avatar"/>`
            }
            temp += `<a href="#" onclick="selectUser(${user.id})" id="userId${user.id}">
                    <li class="clearfix">
                    ${icon}
                        <div class="about">
                        <div class="name"><span id="usersName">${user.firstName}&nbsp;&nbsp;${user.lastName}</span></div>
                        <div class="status">${me}</div>
                        </div>
                    </li>`
    })

    htmlTag.innerHTML = temp;
}