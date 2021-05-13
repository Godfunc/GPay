create table merchant
(
    id               bigint(20)   not null,
    user_id          bigint(20)   not null comment '当前商户对应的用户',
    name             varchar(512) not null comment '商户名',
    code             varchar(64)  not null comment '商户code',
    type             tinyint(2)   not null comment '商户类型 1商户 2代理',
    plat_private_key text comment '平台私钥',
    plat_public_key  text comment '平台公钥',
    public_key       text comment '商户公钥',
    status           tinyint(2)   not null default 1 comment '状态 0禁用 1启用',
    create_id        bigint(20)   not null comment '创建人id',
    update_id        bigint(20) comment '更新人id',
    create_time      datetime     not null comment '创建时间',
    update_time      datetime     null comment '更新时间',
    rm_tag           tinyint(2)   not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_user_id (user_id),
    unique uq_code (code),
    index idx_create_time (create_time)
) comment '商户信息';

create table merchant_balance
(
    id                 bigint(20) not null,
    merchant_id        bigint(20) not null comment '商户id',
    balance_amount     bigint(20) not null default 0 comment '余额',
    frozen_amount      bigint(20) not null default 0 comment '冻结金额',
    last_modify_time   datetime   null comment '最后变更时间',
    last_modify_amount bigint(20) null comment '最后变更金额',
    create_time        datetime   not null comment '创建时间',
    update_time        datetime   null comment '更新时间',
    rm_tag             tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_merchant_id (merchant_id),
    index idx_create_time (create_time),
    index idx_last_modify_time (last_modify_time)
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
    create_time   datetime   not null comment '创建时间',
    update_time   datetime   null comment '更新时间',
    rm_tag        tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index idx_merchant_id (merchant_id),
    index idx_create_time (create_time)
) comment '商户余额变更记录';

create table merchant_agent_info
(
    id             bigint(20) not null,
    merchant_id    bigint(20) not null comment '商户id',
    agent_one_id   bigint(20) not null comment '一级代理',
    agent_two_id   bigint(20) null comment '二级代理',
    agent_three_id bigint(20) null comment '三级代理',
    create_id      bigint(20) not null comment '创建人id',
    update_id      bigint(20) comment '更新人id',
    create_time    datetime   not null comment '创建时间',
    update_time    datetime   null comment '更新时间',
    rm_tag         tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_merchant_id (merchant_id)
) comment '商户代理信息';

create table pay_category
(
    id          bigint(20)   not null,
    name        varchar(512) not null comment '名称',
    code        varchar(64)  not null comment '编号',
    status      tinyint(2)   not null default 1 comment '状态 0禁用 1启用',
    create_id   bigint(20)   not null comment '创建人id',
    update_id   bigint(20) comment '更新人id',
    create_time datetime     not null comment '创建时间',
    update_time datetime     null comment '更新时间',
    rm_tag      tinyint(2)   not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    unique uq_code (code),
    index idx_create_time (create_time)
) comment '渠道主类';

create table pay_channel
(
    id            bigint(20)   not null,
    name          varchar(512) not null comment '名称',
    code          varchar(64)  not null comment '编码',
    create_url    text comment '创建订单地址',
    query_url     text comment '查询订单地址',
    notify_url    text comment '回调地址',
    pay_type_info text comment '支付类型信息',
    logical_tag   varchar(128) not null comment '逻辑标识',
    cost_rate     float(8, 6)  not null comment '成本费率',
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
    id           bigint(20)   not null,
    channel_id   bigint(20)   not null comment '渠道子类id',
    name         varchar(512) null comment '渠道子类商户名',
    account_code varchar(64)  not null comment '渠道子类账号商户号',
    key_info     text comment '渠道子类商户密钥',
    weight       int(4)       not null default 1 comment '权重',
    status       tinyint(2)   not null default 1 comment '状态 0禁用 1启用',
    risk_type    tinyint(4)   not null comment '风控设置 0不设置，1使用通道的风控，2使用自定义风控',
    create_id    bigint(20)   not null comment '创建人id',
    update_id    bigint(20) comment '更新人id',
    create_time  datetime     not null comment '创建时间',
    update_time  datetime     null comment '更新时间',
    rm_tag       tinyint(2)   not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index idx_account_code (account_code),
    index idx_channel_code (channel_id),
    index idx_create_time (create_time)
) comment '渠道子类账号';

create table pay_category_channel
(
    id          bigint(20) not null,
    category_id bigint(20) not null comment '渠道主类id',
    channel_id  bigint(20) not null comment '渠道子类id',
    create_id   bigint(20) not null comment '创建人id',
    update_id   bigint(20) comment '更新人id',
    create_time datetime   not null,
    update_time datetime   null,
    primary key (id),
    unique uq_category_code_channel_code (category_id, channel_id),
    index idx_category_code (category_id),
    index idx_channel_code (channel_id)
) comment '渠道关联';

create table merchant_channel_rate
(
    id              bigint(20)  not null,
    merchant_code   varchar(64) not null comment '商户code',
    pay_category_id bigint(20)  not null comment '渠道主类',
    pay_channel_id  bigint(20)  not null comment '渠道子类',
    rate            float(8, 6) not null comment '费率',
    create_id       bigint(20)  not null comment '创建人id',
    update_id       bigint(20) comment '更新人id',
    create_time     datetime    not null comment '创建时间',
    update_time     datetime    null comment '更新时间',
    rm_tag          tinyint(2)  not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index idx_merchant_code_category_code_channel_code (merchant_code, pay_category_id, pay_channel_id),
    index idx_create_time (create_time)
) comment '商户渠道费率';

create table merchant_risk
(
    id             bigint(20)  not null,
    merchant_code  varchar(64) not null comment '商户code',
    one_amount_min bigint(20)  null comment '单笔最小',
    one_amount_max bigint(20)  null comment '单笔最大 ',
    day_start_time time        not null default '00:00:00' comment '交易开始时间',
    day_end_time   time        not null default '23:59:59' comment '交易结束时间',
    status         tinyint(2)  not null default 1 comment '状态 0禁用 1启用',
    create_id      bigint(20)  not null comment '创建人id',
    update_id      bigint(20) comment '更新人id',
    create_time    datetime    not null comment '创建时间',
    update_time    datetime    null comment '更新时间',
    rm_tag         tinyint(2)  not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index idx_create_time (create_time),
    index idx_merchant_code (merchant_code)
) comment '商户风控';

create table channel_risk
(
    id                 bigint(20) not null,
    channel_id         bigint(64) not null comment '渠道子类id',
    channel_account_id bigint(20) null comment '渠道子类商户id',
    day_amount_max     bigint(20) null comment '每日最大限额',
    one_amount_max     bigint(20) null comment '单笔最大限额',
    one_amount_min     bigint(20) null comment '单笔最小限额 ',
    one_amount         text       null comment '指定单笔金额，多个用,分割',
    day_start_time     time       not null default '00:00:00' comment '交易开始时间',
    day_end_time       time       not null default '23:59:59' comment '交易结束时间',
    status             tinyint(2) not null default 1 comment '状态 0禁用 1启用',
    create_id          bigint(20) not null comment '创建人id',
    update_id          bigint(20) comment '更新人id',
    create_time        datetime   not null comment '创建时间',
    update_time        datetime   null comment '更新时间',
    rm_tag             tinyint(2) not null default 0 comment '删除标识 0正常 1删除',
    primary key (id),
    index idx_create_time (create_time),
    index channel_code (channel_id)
) comment '渠道子类风控';