PGDMP     *                    x            minhasfinancas    12.3    12.3     
           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    57897    minhasfinancas    DATABASE     �   CREATE DATABASE minhasfinancas WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';
    DROP DATABASE minhasfinancas;
                postgres    false            �            1259    57901    usuario    TABLE     �   CREATE TABLE financas.usuario (
    id bigint NOT NULL,
    nome character varying(150),
    email character varying(100),
    senha character varying(20),
    data_cadastro date DEFAULT now()
);
    DROP TABLE financas.usuario;
       financas         heap    postgres    false            �            1259    57899    usuario_id_seq    SEQUENCE     y   CREATE SEQUENCE financas.usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE financas.usuario_id_seq;
       financas          postgres    false    204                       0    0    usuario_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE financas.usuario_id_seq OWNED BY financas.usuario.id;
          financas          postgres    false    203            �
           2604    57904 
   usuario id    DEFAULT     l   ALTER TABLE ONLY financas.usuario ALTER COLUMN id SET DEFAULT nextval('financas.usuario_id_seq'::regclass);
 ;   ALTER TABLE financas.usuario ALTER COLUMN id DROP DEFAULT;
       financas          postgres    false    203    204    204                      0    57901    usuario 
   TABLE DATA           J   COPY financas.usuario (id, nome, email, senha, data_cadastro) FROM stdin;
    financas          postgres    false    204                     0    0    usuario_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('financas.usuario_id_seq', 2, true);
          financas          postgres    false    203            �
           2606    57907    usuario usuario_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY financas.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY financas.usuario DROP CONSTRAINT usuario_pkey;
       financas            postgres    false    204               A   x�3�,-.M,�̇�����9z����1~�FF��F��\F�U�%q��e$"�s��qqq �As     