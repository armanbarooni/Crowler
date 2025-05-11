--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

-- Started on 2024-12-27 20:03:47

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 233 (class 1255 OID 49619)
-- Name: encfunc(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.encfunc() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ 
   BEGIN 
NEW.hashval = md5(NEW.val);
       RETURN NEW; 
   END; 
$$;


ALTER FUNCTION public.encfunc() OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 49620)
-- Name: Seq_tenable; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Seq_tenable"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 100000000000
    CACHE 1;


ALTER TABLE public."Seq_tenable" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 49621)
-- Name: Tenable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Tenable" (
    "CVE" character varying(64) NOT NULL,
    agent character varying(64),
    family character varying(256),
    soloution character varying(1024),
    synopsis character varying(1024),
    title character varying(256),
    date bigint,
    "Vendor" character varying(128) NOT NULL,
    "Products" character varying(128) NOT NULL,
    "Versions" character varying(128) NOT NULL,
    "ID" integer NOT NULL
);


ALTER TABLE public."Tenable" OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 49626)
-- Name: Tenable_vulns; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Tenable_vulns" (
    vulns_id integer NOT NULL,
    tenable_id integer NOT NULL
);


ALTER TABLE public."Tenable_vulns" OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 49629)
-- Name: assets_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.assets_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.assets_seq OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 49630)
-- Name: assets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assets (
    id integer DEFAULT nextval('public.assets_seq'::regclass) NOT NULL,
    scan_name character varying(256) NOT NULL,
    ip_address character varying(256) NOT NULL,
    port_id integer NOT NULL,
    port_protocol character varying(64) NOT NULL,
    service_name character varying(64) NOT NULL,
    product_name character varying(64) NOT NULL,
    version character varying(64) NOT NULL,
    info character varying(64) NOT NULL
);


ALTER TABLE public.assets OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 49636)
-- Name: vulns_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vulns_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vulns_seq OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 49637)
-- Name: vulns; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vulns (
    id integer DEFAULT nextval('public.vulns_seq'::regclass) NOT NULL,
    cve character varying(64) NOT NULL,
    reported_at bigint NOT NULL,
    updated_at bigint NOT NULL,
    description text NOT NULL,
    patch text NOT NULL,
    cvss double precision NOT NULL,
    reference text NOT NULL,
    cwe character varying(64),
    package character varying(128) NOT NULL,
    access_complexity character varying(64),
    attack_vector character varying(64),
    authentication character varying(64),
    impact_type character varying(64),
    integrity_impact character varying(64),
    confidentiality_impact character varying(64),
    availability_impact character varying(64),
    privileges_require character varying(64)
);


ALTER TABLE public.vulns OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 49643)
-- Name: cve; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.cve AS
 SELECT t.id,
    t.cve,
    t.reported_at,
    t.updated_at,
    t.description,
    t.patch,
    t.cvss,
    t.reference,
    t.cwe,
    t.package,
    t.access_complexity,
    t.attack_vector,
    t.authentication,
    t.impact_type,
    t.integrity_impact,
    t.confidentiality_impact,
    t.availability_impact,
    t.privileges_require,
    p.agent,
    p.family,
    p.soloution,
    p.synopsis,
    p.title
   FROM (public.vulns t
     LEFT JOIN public."Tenable" p ON (((p."CVE")::text = (t.cve)::text)));


ALTER TABLE public.cve OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 49648)
-- Name: helping_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.helping_table (
    id integer NOT NULL,
    package character varying(128) NOT NULL,
    product_name character varying(128) NOT NULL
);


ALTER TABLE public.helping_table OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 49651)
-- Name: log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log (
    user_id integer,
    event_result smallint NOT NULL,
    event_type text NOT NULL,
    browser_name text NOT NULL,
    form_name text,
    port integer,
    id integer NOT NULL,
    start_date bigint NOT NULL,
    finish_date bigint NOT NULL,
    ip text
);


ALTER TABLE public.log OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 49656)
-- Name: log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.log ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 49657)
-- Name: lookups_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lookups_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lookups_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 49658)
-- Name: product_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.product_seq OWNER TO postgres;

CREATE TABLE public.product (
    id integer DEFAULT nextval('public.product_seq'::regclass) NOT NULL,
    product_name character varying(128) NOT NULL,
    version character varying(64) NOT NULL,
    MinVersion character varying(64) NOT NULL,
    MaxVersion character varying(64) NOT NULL,
    vulns_id integer NOT NULL
);

ALTER TABLE public.product ADD CONSTRAINT product_pkey PRIMARY KEY (id);

ALTER TABLE public.product ADD CONSTRAINT unique_product_entry
    UNIQUE (product_name, version, MinVersion, MaxVersion, vulns_id);

ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 49663)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 49668)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.role ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 225 (class 1259 OID 49669)
-- Name: setting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.setting (
    id integer NOT NULL,
    expire_date integer NOT NULL,
    lock_time_duration integer,
    max_failed_attempts integer
);


ALTER TABLE public.setting OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 57810)
-- Name: subscribed_vendors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscribed_vendors (
    product_name character varying(64),
    user_id integer,
    email character varying(64)
);


ALTER TABLE public.subscribed_vendors OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 49672)
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id integer NOT NULL,
    first_name text NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    last_name text NOT NULL,
    created_date text,
    expire_date text,
    last_login text,
    failed_attempt integer DEFAULT 0,
    account_non_locked boolean DEFAULT false,
    lock_time text DEFAULT 0,
    is_enabled boolean
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 49680)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."user" ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 49681)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 49684)
-- Name: vendor_product; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vendor_product AS
 SELECT DISTINCT t.package,
    p1.product_name
   FROM (public.vulns t
     JOIN public.product p1 ON ((t.id = p1.vulns_id)));


ALTER TABLE public.vendor_product OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 49688)
-- Name: vendor_product_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vendor_product_table (
    vendor character varying NOT NULL,
    product character varying NOT NULL
);


ALTER TABLE public.vendor_product_table OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 49693)
-- Name: vuln_vendor_product; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vuln_vendor_product AS
 SELECT t.id,
    t.package,
    p.product_name
   FROM (public.vulns t
     JOIN public.product p ON ((p.vulns_id = t.id)));


ALTER TABLE public.vuln_vendor_product OWNER TO postgres;

ALTER TABLE public.vulns
ADD CONSTRAINT unique_package_cve UNIQUE (package, cve);

--
-- TOC entry 3421 (class 0 OID 49621)
-- Dependencies: 210
-- Data for Name: Tenable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Tenable" ("CVE", agent, family, soloution, synopsis, title, date, "Vendor", "Products", "Versions", "ID") FROM stdin;
\.


--
-- TOC entry 3422 (class 0 OID 49626)
-- Dependencies: 211
-- Data for Name: Tenable_vulns; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Tenable_vulns" (vulns_id, tenable_id) FROM stdin;
\.


--
-- TOC entry 3424 (class 0 OID 49630)
-- Dependencies: 213
-- Data for Name: assets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.assets (id, scan_name, ip_address, port_id, port_protocol, service_name, product_name, version, info) FROM stdin;
\.


--
-- TOC entry 3427 (class 0 OID 49648)
-- Dependencies: 217
-- Data for Name: helping_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.helping_table (id, package, product_name) FROM stdin;
\.


--
-- TOC entry 3428 (class 0 OID 49651)
-- Dependencies: 218
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.log (user_id, event_result, event_type, browser_name, form_name, port, id, start_date, finish_date, ip) FROM stdin;
\.


--
-- TOC entry 3432 (class 0 OID 49659)
-- Dependencies: 222
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, product_name, version, vulns_id) FROM stdin;
\.


--
-- TOC entry 3433 (class 0 OID 49663)
-- Dependencies: 223
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
1	ROLE_MANAGER
2	ROLE_EXPERT
3	ROLE_SUPERVISOR
4	ROLE_ORDINARY
\.


--
-- TOC entry 3435 (class 0 OID 49669)
-- Dependencies: 225
-- Data for Name: setting; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.setting (id, expire_date, lock_time_duration, max_failed_attempts) FROM stdin;
1	6	1	10
\.


--
-- TOC entry 3440 (class 0 OID 57810)
-- Dependencies: 232
-- Data for Name: subscribed_vendors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subscribed_vendors (product_name, user_id, email) FROM stdin;
\.


--
-- TOC entry 3436 (class 0 OID 49672)
-- Dependencies: 226
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, first_name, username, password, last_name, created_date, expire_date, last_login, failed_attempt, account_non_locked, lock_time, is_enabled) FROM stdin;
1	admin	postgres	3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4	ادمین	1611084294012	1611084294012	1611084294012	0	t	0	t
14	nazer	nazer	$2a$10$7lAdQzs1OWAwFnmN/n/8n.uKW5WpmMI6ZSEB1KRSaYEaxurae/uiO	nazer	1620463854446	1620463912260	1620463926937	0	t	0	t
15	khebre	khebre	$2a$10$Tisj9QQeH/FYWrGGVSa.nOh3KnqjiWpyGtu2C2ZwqX2vWNtu0BxD.	khebre	1620463873126	1620464035913	1620464046571	0	t	0	t
13	آرمان	arman	$2a$10$4dqLhWkqGiz5/NGmmXfR8ulKDI3cPbkK7NgrDzUr6V1z1enr0MWJu	بارونی	1619919865418	1627160842780	1627168528823	0	t	0	t
2	رضا	manager	$2a$10$3vTMwkmW5YN3Ck8Vd2KwpOwec20nhtt/8UMIuivxOqiD93T/xcbIS	مدیر	1611084294012	1689357461136	1700846875295	0	t	0	t
\.


--
-- TOC entry 3438 (class 0 OID 49681)
-- Dependencies: 228
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_roles (user_id, role_id) FROM stdin;
2	1
13	4
14	3
15	2
\.


--
-- TOC entry 3439 (class 0 OID 49688)
-- Dependencies: 230
-- Data for Name: vendor_product_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vendor_product_table (vendor, product) FROM stdin;
\.


--
-- TOC entry 3426 (class 0 OID 49637)
-- Dependencies: 215
-- Data for Name: vulns; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vulns (id, cve, reported_at, updated_at, description, patch, cvss, reference, cwe, package, access_complexity, attack_vector, authentication, impact_type, integrity_impact, confidentiality_impact, availability_impact, privileges_require) FROM stdin;
\.


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 209
-- Name: Seq_tenable; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Seq_tenable"', 1, false);


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 212
-- Name: assets_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.assets_seq', 1, false);


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 219
-- Name: log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_id_seq', 1508, true);


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 220
-- Name: lookups_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.lookups_seq', 15063892, true);


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 221
-- Name: product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_seq', 75067346, true);


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 224
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 4, true);


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 227
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 15, true);


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 214
-- Name: vulns_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vulns_seq', 4214326, true);


--
-- TOC entry 3239 (class 2606 OID 49699)
-- Name: Tenable Tenable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Tenable"
    ADD CONSTRAINT "Tenable_pkey" PRIMARY KEY ("CVE", "Vendor", "Products", "Versions", "ID");


--
-- TOC entry 3241 (class 2606 OID 49701)
-- Name: Tenable_vulns Tenable_vulns_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Tenable_vulns"
    ADD CONSTRAINT "Tenable_vulns_pkey" PRIMARY KEY (vulns_id, tenable_id);


--
-- TOC entry 3243 (class 2606 OID 49703)
-- Name: assets assets_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assets
    ADD CONSTRAINT assets_id_key UNIQUE (id);


--
-- TOC entry 3245 (class 2606 OID 49705)
-- Name: assets assets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assets
    ADD CONSTRAINT assets_pkey PRIMARY KEY (scan_name, ip_address, port_id, product_name);


--
-- TOC entry 3255 (class 2606 OID 49707)
-- Name: helping_table helping_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.helping_table
    ADD CONSTRAINT helping_table_pkey PRIMARY KEY (id, package, product_name);


--
-- TOC entry 3257 (class 2606 OID 49709)
-- Name: log log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (id);


--
-- TOC entry 3259 (class 2606 OID 49711)
-- Name: product product_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_id_key UNIQUE (id);


--
-- TOC entry 3262 (class 2606 OID 49727)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_name, version, vulns_id);


--
-- TOC entry 3264 (class 2606 OID 49729)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 3266 (class 2606 OID 49731)
-- Name: setting setting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.setting
    ADD CONSTRAINT setting_pkey PRIMARY KEY (id);


--
-- TOC entry 3268 (class 2606 OID 49733)
-- Name: user username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT username UNIQUE (username);


--
-- TOC entry 3270 (class 2606 OID 49735)
-- Name: user users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3272 (class 2606 OID 49737)
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3274 (class 2606 OID 49739)
-- Name: vendor_product_table vendor_product_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vendor_product_table
    ADD CONSTRAINT vendor_product_table_pkey PRIMARY KEY (vendor, product);


--
-- TOC entry 3251 (class 2606 OID 49741)
-- Name: vulns vulns_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vulns
    ADD CONSTRAINT vulns_id_key UNIQUE (id);


--
-- TOC entry 3253 (class 2606 OID 49743)
-- Name: vulns vulns_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vulns
    ADD CONSTRAINT vulns_pkey PRIMARY KEY (cve, package);


--
-- TOC entry 3249 (class 1259 OID 49744)
-- Name: ids; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ids ON public.vulns USING btree (id);


--
-- TOC entry 3246 (class 1259 OID 49745)
-- Name: ip; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ip ON public.assets USING btree (ip_address);


--
-- TOC entry 3247 (class 1259 OID 49746)
-- Name: port; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX port ON public.assets USING btree (port_id);


--
-- TOC entry 3248 (class 1259 OID 49747)
-- Name: product_asset; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX product_asset ON public.assets USING btree (product_name);


--
-- TOC entry 3260 (class 1259 OID 49748)
-- Name: product_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX product_name ON public.product USING btree (product_name);


--
-- TOC entry 3276 (class 2606 OID 49749)
-- Name: users_roles role_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- TOC entry 3277 (class 2606 OID 49754)
-- Name: users_roles user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3275 (class 2606 OID 49759)
-- Name: log user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id);


-- Completed on 2024-12-27 20:03:48

--
-- PostgreSQL database dump complete
--

