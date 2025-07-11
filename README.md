# Проект асинхронного взаимодействия сервисов ресторана

## Стек технологий

- **Java 21** – язык программирования

- **Spring Boot 3.4.3** – основной фреймворк

- **Spring Data JPA** – работа с базой данных

- **Spring Security** – безопасность и аутентификация

- **Spring Kafka** – взаимодействие через Kafka

- **PostgreSQL** – реляционная база данных

- **Docker** – контейнеризация

- **Maven**  – система сборки

- **Lombok 1.18.30** – удобная работа с кодом

- **Swagger (SpringDoc OpenAPI 2.2.0)** – автодокументация API


## Общее описание

Этот проект реализует логику работы ресторана через микросервисную архитектуру. В нем существует три основных сервиса:

- **Restaurant-service** – управление официантами (Waiter), администраторами (Admin), меню (Category, Dish), столиками (RestTable, RestTableItem) и заказами (Order, OrderDetail)

- **Payment-service** – эмуляция обработки платежей с фиксацией транзаций (PaymentTransaction) в БД.

- **Kitchen-service** – эмуляция работы кухни.

Каждый из сервисов является отдельным модулем, имеющим свою бизнес-логику и взаимодействующим с другими сервисами через Apache Kafka.

**Kafka (Zookeeper, Kafka, KafkaUI), restaurant-service, payment-service, kitchen-service контейнеризированы через docker-compose. Restaurant-service имеет скрипт, который заполняет БД данными**
## Детальное описание архитектуры

Проект построен по принципу многоуровневой зависимости:

1. **Родительский проект:** `liga-restaraunt`

    - Объединяет все модули в один проект.

2. **Общие зависимости:** `dependency-bom`

    - Описывает набор версий библиотек, которые используются во всех сервисах.

3. **Общие DTO:** `kafka-dto`

    - Содержит объекты, передаваемые через Kafka.

4. **Основные микросервисы:**

    - **`restaurant-service`** ([Swagger](http://localhost:8080/swagger-ui/index.html#))

    - **`payment-service`** ([Swagger](http://localhost:8081/swagger-ui/index.html#))

    - **`kitchen-service`**


Микросервисы зависят от `kafka-dto`, чтобы обмениваться общими объектами и данными.

При каждом изменении статуса заказа и времени готовности данные заказа обновляются в БД через взаимодействия в Kafka

### 1. Описание работы модуля `restaraunt-service`:

Этот модуль описывает поведение **официантов** (Waiter), **администраторов** (Admin), **меню** (Category, Dish), **столиков** (RestTable, RestTableItem) и **заказов**, создаваемых официантами (Order, OrderDetail). Все эндпоинты описаны через Swagger, доступный по ссылке: `http://localhost:8080/swagger-ui/index.html#/`

### Пользовательский путь (официант):

1. **Авторизация**: Официант и администратор могут авторизоваться через общий логин (basic-auth) по своему номеру телефона и паролю.  
   Для работы с приложением используйте:

    - **username** = любой из базы (для админа +71001001010, для официанта +72002002020)

    - **password** = 1234  
      Либо зайдите за админа и создайте официанта со своими собственными учетными данными.

2. **Взятие столика**: Официант может взять на себя любой свободный столик через **POST /tables/{id}/assign**, если:

    - столик не занят другим официантом.

3. **Заполнение заявки**: Официант заполняет заявку столика через **POST /tables/{number}/calculate**, добавляя одно блюдо за раз.

    - Нельзя добавить количество блюда, которое превышает `stockQuantity` этого блюда, расчитываемое через суммарное количество блюда, которое уже есть и добавлено + то количество, которое объявлено в запросе.

    - Если в теле запроса для блюда, которое уже есть в заявке, указать значение, суммарно равное <= 0, то оно удалится из заявки.

4. **Создание заказа**: После того как все товары добавлены, официант может создать заказ через **POST /orders/create-table-order/{tableNumber}**.  
   При создании заказа:

    - Для блюда количество на складе уменьшается на то количество, которое было заказано.

    - Если `stockQuantity = 0` у блюда при создании заказа, оно становится недоступным (`isAvailable = false`), и его больше нельзя добавить в другие заявки.

    - Заявка столика очищается (список `RestTableItems` у `RestTable` становится пустым).

    - Заказу присваивается статус `CREATED`, статус оплаты `NOT_PAID` и текущие дата и время создания заказа (**createTime**).

    - Хранение деталей заказа реализовано через список объектов класса OrderDetail, который сохраняет заявку столика.

5. **Подача заказа**: Когда заказ из сервиса кухни получает статус `READY` (пройдя полный цикл оплаты и приготовления на кухне), официант, обслуживающий данный столик, может подать заказ через **POST /orders/serve-to-table**.

    - Если у текущего столика при данном официанте больше нет заказов, данный столик отвязывается от официанта и снова может быть занят.

    - Если у столика еще есть заказы в работе, столик остается за официантом.


### Описание работы администратора:

Администратор может выполнять следующие действия:

- **Создать официанта**.
- **Получить данные конкретного официанта**.
- **Получить список заказов**, созданных конкретным официантом.
- **Получить список всех официантов**.
- **Изменить данные конкретного официанта**, с возможностью изменить его пароль через отдельный метод.
- **Удалить официанта**.
- **Восстановить официанта**.
- **Получить данные авторизованного админа**.
- **Изменить данные авторизованного админа**, с возможностью изменить его пароль через отдельный метод.
- **Получить список всех столиков**.
- **Получить столики**, взятые конкретным официантом.
- **Снять официанта со столика**.
- **Получить все заказы**.
- **Получить конкретный заказ**.
- **Получить все заказы**, сделанные конкретным официантом.


### Описание работы официанта:

Официант может выполнять следующие действия:

- **Получить свои данные**.
- **Изменить свои данные**, с возможностью изменить его пароль через отдельный метод.
- **Получить список всех столиков, взятых официантом**
- **Получить список всех созданных этим официантом заказов**.
- **Взять на себя столик,** если он не занят другим официантом
- **Добавить блюдо / Изменить количество блюд / Удалить блюдо** в заявке столика
- **Очистить заявку столика**
- **Создать заказ столика**
- **Повторить оплату заказа**
- **Подать готовый заказ на стол**

### Общие функции:

- **Получить список всех блюд**
- **Получить конкретное блюдо по id**
- **Получить все активные блюда**, т.е. у которых isDeleted = false и isAvailable = true
- **Получить список всех категорий**
- **Получить конкретную категорию по id**
- **Получить все блюда конкретной категории**


### 2. Описание модуля **payment-service**:

Этот модуль реализует эмуляцию оплаты созданного официантом заказа (OrderDTO) за конкретным столиком. Также модуль фиксирует все транзакции (PaymentTransaction) заказа и записывает их в базу данных оплат со статусами SUCCESS или FAILED.

Если в этот сервис через Kafka пришел заказ, у которого:

- сумма заказа не равна 0,

- заявка (List\<OrderDetailDTO>) не пуста,

- статус заказа **CREATED**,

- статус оплаты **NOT_PAID**,


тогда заказу присваиваются следующие статусы:

- статус заказа **SENT_TO_KITCHEN**,

- статус оплаты **PAID**,


После чего заказ отправляется готовиться на кухню в модуль **kitchen-service** через Kafka-топик `order-cooking`.


#### Описание эндпоинтов модуля:

В модуле **payment-service** реализован следующий функционал, доступный через [Swagger UI](http://localhost:8081/swagger-ui/index.html#):

1. **GET /transactions** – Получить все транзакции.

2. **GET /transactions/{id}** – Получить транзакцию по **id**.

3. **GET /transactions/order/{orderId}** – Получить список транзакций (со статусами **FAILED** или **SUCCESS**) для конкретного заказа.


### 3. Описание модуля `kitchen-service`

Этот модуль эмулирует работу кухни, которая готовит заказ после его оплаты. Модуль принимает `OrderDTO` через событие Kafka-топика `order-cooking`. Если у заказа:

- статус `SENT_TO_KITCHEN` (заказ был передан на кухню) и

- статус оплаты `PAID` (заказ был оплачен)

- если заявка заказа (`List<OrderDetailDTO>`) не пуста (т.е. содержит хотя бы одно блюдо)


В таком случае заказу присваивается статус `COOKING`, что означает, что заказ находится в процессе приготовления. После того как заказ готов, его статус меняется на `READY`, что позволяет официанту подать заказ через основной модуль.


### 4. Описание модуля `dependency-bom`

Модуль `dependency-bom` содержит все общие зависимости для других микросервисов в проекте. Это централизованное место для управления версиями библиотек и фреймворков, что позволяет поддерживать согласованность версий и упрощает обновление зависимостей в проекте.


### 5. Описание модуля `kafka-dto`

Модуль `kafka-dto` содержит все общие DTO и объекты, которые используются для обмена данными между микросервисами через Kafka. Эти объекты данных определяют структуру сообщений, передаваемых между сервисами, и обеспечивают совместимость между ними.
