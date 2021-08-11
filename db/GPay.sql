create table merchant
(
    id               bigint(20) not null,
    user_id          bigint(20) not null comment '当前商户对应的用户',
    name             varchar(512) not null comment '商户名',
    code             varchar(64)  not null comment '商户code',
    type             tinyint(2) not null comment '商户类型 1商户 2代理',
    agent_id         bigint(20) null comment '代理id',
    plat_private_key text comment '平台私钥',
    plat_public_key  text comment '平台公钥',
    public_key       text comment '商户公钥',
    status           tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    create_id        bigint(20) not null comment '创建人id',
    update_id        bigint(20) comment '更新人id',
    create_time      datetime     not null comment '创建时间',
    update_time      datetime null comment '更新时间',
    rm_tag           tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_user_id (user_id),
    unique uq_code (code),
    index            idx_create_time (create_time)
) comment '商户信息';

create table merchant_balance
(
    id                 bigint(20) not null,
    merchant_id        bigint(20) not null comment '商户id',
    balance_amount     bigint(20) not null default 0 comment '余额',
    frozen_amount      bigint(20) not null default 0 comment '冻结金额',
    last_modify_time   datetime null comment '最后变更时间',
    last_modify_amount bigint(20) null comment '最后变更金额',
    create_time        datetime not null comment '创建时间',
    update_time        datetime null comment '更新时间',
    rm_tag             tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_merchant_id (merchant_id),
    index              idx_create_time (create_time),
    index              idx_last_modify_time (last_modify_time)
) comment '商户余额';

create table merchant_balance_record
(
    id            bigint(20) not null,
    merchant_id   bigint(20) not null comment '商户id',
    old_amount    bigint(20) not null comment '当时金额',
    new_amount    bigint(20) not null comment '变更后金额',
    change_amount bigint(20) not null comment '变更金额',
    type          tinyint(2) not null comment '类型 1订单支付 2提现申请 3手动修改',
    remark        text comment '备注',
    linked_id     bigint(20) not null comment '关联数据',
    create_id     bigint(20) not null comment '创建人id',
    update_id     bigint(20) comment '更新人id',
    create_time   datetime not null comment '创建时间',
    update_time   datetime null comment '更新时间',
    rm_tag        tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index         idx_merchant_id (merchant_id),
    index         idx_create_time (create_time)
) comment '商户余额变更记录';

create table pay_category
(
    id          bigint(20) not null,
    name        varchar(512) not null comment '名称',
    code        varchar(64)  not null comment '编号',
    status      tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    create_id   bigint(20) not null comment '创建人id',
    update_id   bigint(20) comment '更新人id',
    create_time datetime     not null comment '创建时间',
    update_time datetime null comment '更新时间',
    rm_tag      tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_code (code),
    index       idx_create_time (create_time)
) comment '渠道主类';

create table pay_channel
(
    id            bigint(20) not null,
    name          varchar(512) not null comment '名称',
    code          varchar(64)  not null comment '编码',
    create_url    text comment '创建订单地址',
    query_url     text comment '查询订单地址',
    notify_url    text comment '回调地址',
    pay_type_info text comment '支付类型信息',
    logical_tag   varchar(128) not null comment '逻辑标识',
    cost_rate     float(8, 6
) not null comment '成本费率',
    status        tinyint(2)   not null default 1 comment '状态 0禁用 1启用',
    create_id     bigint(20)   not null comment '创建人id',
    update_id     bigint(20) comment '更新人id',
    create_time   datetime     not null comment '创建时间',
    update_time   datetime     null comment '更新时间',
    rm_tag        tinyint(2)   not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_code (code),
    index idx_create_time (create_time)
) comment '渠道子类';

create table pay_channel_account
(
    id           bigint(20) not null,
    channel_id   bigint(20) not null comment '渠道子类id',
    name         varchar(512) null comment '渠道子类商户名',
    account_code varchar(64) not null comment '渠道子类账号商户号',
    key_info     text null comment '渠道子类商户密钥',
    weight       int(4) not null default 1 comment '权重',
    status       tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    risk_type    tinyint(4) not null comment '风控设置 0不设置，1使用通道的风控，2使用自定义风控',
    create_id    bigint(20) not null comment '创建人id',
    update_id    bigint(20) comment '更新人id',
    create_time  datetime    not null comment '创建时间',
    update_time  datetime null comment '更新时间',
    rm_tag       tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index        idx_account_code (account_code),
    index        idx_channel_id (channel_id),
    index        idx_create_time (create_time)
) comment '渠道子类账号';

create table pay_category_channel
(
    id          bigint(20) not null,
    category_id bigint(20) not null comment '渠道主类id',
    channel_id  bigint(20) not null comment '渠道子类id',
    weight      int(4) not null default 1 comment '权重',
    create_id   bigint(20) not null comment '创建人id',
    update_id   bigint(20) comment '更新人id',
    create_time datetime not null,
    update_time datetime null,
    primary key (id),
    unique uq_category_id_channel_id (category_id, channel_id),
    index       idx_category_id (category_id),
    index       idx_channel_id (channel_id)
) comment '渠道关联';

create table merchant_channel_rate
(
    id              bigint(20) not null,
    merchant_id     bigint(20) not null comment '商户id',
    merchant_code   varchar(64) not null comment '商户code',
    pay_category_id bigint(20) not null comment '渠道主类id',
    pay_channel_id  bigint(20) not null comment '渠道主类id',
    rate            float(8, 6
) not null comment '费率',
    create_id       bigint(20)  not null comment '创建人id',
    update_id       bigint(20) comment '更新人id',
    create_time     datetime    not null comment '创建时间',
    update_time     datetime    null comment '更新时间',
    primary key (id),
    unique uq_merchant_code_category_id_channel_id (merchant_code, pay_category_id, pay_channel_id),
    index idx_create_time (create_time)
) comment '商户渠道费率';

create table merchant_risk
(
    id             bigint(20) not null,
    merchant_id    bigint(20) not null comment '商户id',
    merchant_code  varchar(64) not null comment '商户code',
    one_amount_min bigint(20) null comment '单笔最小',
    one_amount_max bigint(20) null comment '单笔最大 ',
    day_start_time time        not null default '00:00:00' comment '交易开始时间',
    day_end_time   time        not null default '23:59:59' comment '交易结束时间',
    status         tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    create_id      bigint(20) not null comment '创建人id',
    update_id      bigint(20) comment '更新人id',
    create_time    datetime    not null comment '创建时间',
    update_time    datetime null comment '更新时间',
    rm_tag         tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index          idx_create_time (create_time),
    index          idx_merchant_code (merchant_code),
    index          idx_merchant_id (merchant_id)
) comment '商户风控';

create table pay_order_detail
(
    id                           bigint(20) not null,
    order_id                     bigint(20) not null comment '订单id',
    merchant_id                  bigint(20) not null comment '商户id',
    merchant_code                varchar(64)  not null comment '商户号',
    merchant_name                varchar(512) not null comment '商户名',
    pay_category_id              bigint(20) not null comment '渠道主类id',
    pay_channel_id               bigint(20) not null comment '渠道子类id',
    plat_private_key             text comment '平台私钥',
    pay_channel_account_id       bigint(20) not null comment '渠道账号id',
    pay_channel_account_code     varchar(64)  not null comment '渠道账号商户号',
    pay_channel_account_key_info text null comment '渠道账号密钥',
    pay_channel_day_max          bigint(20) null comment '渠道每日最大限额',
    pay_channel_account_day_max  bigint(20) null comment '账号每日最大限额',
    channel_create_url           varchar(64)  not null comment '渠道下单地址',
    channel_query_url            varchar(64) null comment '渠道查询订单地址',
    channel_notify_url           varchar(64)  not null comment '渠道回调地址',
    channel_pay_type_info        text comment '支付类型信息',
    channel_cost_rate            float(8, 6
) not null comment '费率',
    logical_tag                  varchar(128) not null comment '逻辑标识',
    order_expired_time           datetime     null comment '订单过期时间',
    ua_type                      tinyint(4)   null comment '客户端类型',
    ua_str                       text         null comment '客户端ua',
    client_ip                    varchar(128) not null comment '下单客户ip',
    pay_client_ip                varchar(128) null comment '支付客户端ip',
    good_name                    text         null comment '商品名称',
    create_time                  datetime     not null comment '创建时间',
    primary key (id),
    unique uq_order_id (order_id),
    index idx_pay_category_id (pay_category_id),
    index idx_pay_channel_id (pay_channel_id),
    index idx_pay_channel_account_id (pay_channel_account_id),
    index idx_logical_tag (logical_tag),
    index idx_create_time (create_time)
) comment '订单详情表';

create table channel_risk
(
    id                 bigint(20) not null,
    channel_id         bigint(64) not null comment '渠道子类id',
    channel_account_id bigint(20) null comment '渠道子类商户id',
    day_amount_max     bigint(20) null comment '每日最大限额',
    one_amount_max     bigint(20) null comment '单笔最大限额',
    one_amount_min     bigint(20) null comment '单笔最小限额 ',
    one_amount         text null comment '指定单笔金额，多个用,分割',
    day_start_time     time     not null default '00:00:00' comment '交易开始时间',
    day_end_time       time     not null default '23:59:59' comment '交易结束时间',
    status             tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    create_id          bigint(20) not null comment '创建人id',
    update_id          bigint(20) comment '更新人id',
    create_time        datetime not null comment '创建时间',
    update_time        datetime null comment '更新时间',
    rm_tag             tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index              idx_create_time (create_time),
    index              channel_id (channel_id)
) comment '渠道子类风控';

create table pay_order
(
    id                   bigint(20) not null,
    merchant_id          bigint(20) not null comment '商户id',
    merchant_code        varchar(64)  not null comment '商户号',
    merchant_name        varchar(512) not null comment '商户名',
    out_trade_no         varchar(64)  not null comment '商户单号',
    order_no             varchar(64)  not null comment '平台单号',
    trade_no             varchar(64) null comment '上游单号',
    channel_account_code varchar(64)  not null comment '渠道账号商户号',
    amount               bigint(20) not null comment '订单金额',
    real_amount          bigint(20) not null comment '实际支付金额',
    client_create_time   datetime     not null comment '客户端创建时间',
    create_time          datetime     not null comment '订单创建时间',
    pay_time             datetime null comment '订单支付时间',
    pay_type             varchar(64) null comment '支付类型信息',
    pay_str              text null comment '支付链接',
    notify_time          datetime null comment '回调时间',
    notify_url           text null comment '回调地址',
    status               tinyint(4) not null default 1 comment '订单状态 1.已下单 2.已扫码 3.已支付 4.已回调',
    update_time          datetime null comment '更新时间',
    primary key (id),
    unique uq_out_trade_no_merchant_code (merchant_code, out_trade_no),
    unique uq_channel_account_code_trade_no (channel_account_code, trade_no),
    index                idx_create_time (create_time),
    index                idx_pay_time (pay_time),
    index                idx_notify_time (notify_time)
) comment '订单表';

create table pay_merchant_order_profit
(
    id                    bigint(20) not null,
    order_id              bigint(20) not null comment '订单id',
    order_amount          bigint(20) not null comment '订单金额',
    merchant_id           bigint(20) not null comment '商户id',
    merchant_code         varchar(64) not null comment '商户号',
    merchant_channel_rate float(8, 6) not null comment '商户渠道费率',
    profit_amount         bigint(20)  not null comment '收益',
    create_time           datetime    not null comment '创建时间',
    primary key (id),
    unique uq_order_id_merchant_id (order_id, merchant_id),
    index idx_merchant_code (merchant_code),
    index idx_create_time (create_time)
) comment '商户订单收益表';

create table pay_platform_order_profit
(
    id                bigint(20) not null,
    order_id          bigint(20) not null comment '订单id',
    order_amount      bigint(20) not null comment '订单金额',
    merchant_id       bigint(20) not null comment '商户id',
    merchant_code     varchar(64) not null comment '商户号',
    channel_cost_rate float(8, 6) not null comment '渠道成本费率',
    profit_amount       bigint(20)  not null comment '收益',
    channel_cast_amount bigint(20)  not null comment '通道成本',
    create_time         datetime    not null comment '创建时间',
    primary key (id),
    unique uq_order_id (order_id),
    index idx_merchant_code (merchant_code),
    index idx_merchant_id (merchant_id),
    index idx_create_time (create_time)
) comment '平台订单收益表';

create table mg_config
(
    id          bigint(20) not null,
    name        varchar(512) not null comment '名称',
    value       text comment '值',
    remark      text null comment '额外信息',
    create_id   bigint(20) not null comment '创建人id',
    update_id   bigint(20) comment '更新人id',
    create_time datetime     not null comment '创建时间',
    update_time datetime null comment '更新时间',
    primary key (id),
    unique uq_name (name),
    index       idx_create_time (create_time)
) comment '系统配置表';

create table mg_job
(
    id         bigint(20) not null,
    name       varchar(128) not null comment '任务名',
    group_job  varchar(128) not null comment '任务所属组',
    invoke     varchar(256) not null comment '目标任务',
    cron       varchar(512) not null comment '任务cron',
    misfire    tinyint(4) not null default 0 comment 'misfire策略 0默认 1立即执行 2触发一次执行 3不触发执行',
    concurrent tinyint(2) not null default 1 comment '是否允许并发执行 0禁止 1允许',
    status     tinyint(2) not null default 1 comment '状态 0停用 1启用',
    primary key (id),
    unique uq_name_group_job_invoke (name, group_job, invoke)
) comment '任务表';

create table pay_order_log
(
    id          bigint(20) not null,
    merchant_id bigint(20) not null comment '商户',
    order_id    bigint(20) not null comment '订单id',
    old_status  tinyint(4) not null comment '原始订单状态',
    new_status  tinyint(4) not null comment '订单新状态',
    reason      text null comment '状态变更原因',
    result      tinyint(2) not null comment '状态 1成功 2失败',
    create_time datetime not null comment '创建时间',
    primary key (id),
    index       idx_order_id_merchant_id (order_id, merchant_id),
    index       idx_create_time (create_time)
) comment '订单日志表'