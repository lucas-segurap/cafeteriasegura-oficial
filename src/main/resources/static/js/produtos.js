document.addEventListener("DOMContentLoaded", function () {

    // =========================================================
    // FILTRO DOS PRODUTOS
    // =========================================================

    const botoesFiltro = document.querySelectorAll(".filtro-produto");
    const produtos = document.querySelectorAll(".produto-card");
    const contadorProdutos = document.getElementById("contadorProdutos");

    botoesFiltro.forEach(function (botao) {

        botao.addEventListener("click", function () {

            const filtroSelecionado = botao.dataset.filtro;

            // Atualiza botão ativo
            botoesFiltro.forEach(function (b) {
                b.classList.remove("ativo");
                b.setAttribute("aria-pressed", "false");
            });

            botao.classList.add("ativo");
            botao.setAttribute("aria-pressed", "true");

            let quantidadeVisivel = 0;

            // Filtra os produtos
            produtos.forEach(function (produto) {

                const categoria = produto.dataset.categoria;

                if (
                    filtroSelecionado === "todos" ||
                    categoria === filtroSelecionado
                ) {
                    produto.style.display = "";
                    quantidadeVisivel++;
                } else {
                    produto.style.display = "none";
                }

            });

            // Atualiza contador
            if (contadorProdutos) {
                contadorProdutos.textContent = quantidadeVisivel;
            }

        });

    });


    // =========================================================
    // ADICIONAR PRODUTO AO PEDIDO
    // =========================================================

    const botoesAdicionar = document.querySelectorAll(".btn-adicionar");

    botoesAdicionar.forEach(function (botao) {

        botao.addEventListener("click", function () {

            const nome = botao.dataset.produto;
            const preco = parseFloat(botao.dataset.preco);

            // Recupera pedido existente
            let pedido = JSON.parse(localStorage.getItem("pedido")) || [];

            // Verifica se o produto já existe
            const produtoExistente = pedido.find(function (produto) {
                return produto.nome === nome;
            });

            if (produtoExistente) {

                // Aumenta quantidade
                produtoExistente.quantidade++;

            } else {

                // Adiciona novo produto
                pedido.push({
                    nome: nome,
                    preco: preco,
                    quantidade: 1
                });

            }

            // Salva novamente
            localStorage.setItem("pedido", JSON.stringify(pedido));

            // Feedback visual
            const textoOriginal = botao.textContent;

            botao.textContent = "✓ Adicionado";
            botao.disabled = true;

            setTimeout(function () {

                botao.textContent = textoOriginal;
                botao.disabled = false;

            }, 1000);

        });

    });

});
document.addEventListener("DOMContentLoaded", function () {

```
// =========================================================
// FILTROS
// =========================================================

const botoesFiltro = document.querySelectorAll(".filtro-produto");
const produtos = document.querySelectorAll(".produto-card");
const contador = document.getElementById("contadorProdutos");

botoesFiltro.forEach(function (botao) {

    botao.addEventListener("click", function () {

        const filtro = botao.dataset.filtro;

        // Atualiza botão ativo
        botoesFiltro.forEach(function (item) {
            item.classList.remove("ativo");
            item.setAttribute("aria-pressed", "false");
        });

        botao.classList.add("ativo");
        botao.setAttribute("aria-pressed", "true");

        let quantidade = 0;

        produtos.forEach(function (produto) {

            const categoria = produto.dataset.categoria;

            if (filtro === "todos" || categoria === filtro) {

                produto.style.display = "";

                quantidade++;

            } else {

                produto.style.display = "none";

            }

        });

        if (contador) {
            contador.textContent = quantidade;
        }

    });

});


// =========================================================
// ADICIONAR PRODUTO AO PEDIDO
// =========================================================

const botoesAdicionar = document.querySelectorAll(".btn-adicionar");

botoesAdicionar.forEach(function (botao) {

    botao.addEventListener("click", function () {

        const nome = botao.dataset.produto;
        const preco = Number(botao.dataset.preco);

        let pedido = JSON.parse(
            localStorage.getItem("pedido")
        ) || [];


        // Procura o produto
        const produtoExistente = pedido.find(function (produto) {

            return produto.nome === nome;

        });


        if (produtoExistente) {

            produtoExistente.quantidade++;

        } else {

            pedido.push({
                nome: nome,
                preco: preco,
                quantidade: 1
            });

        }


        // Salva
        localStorage.setItem(
            "pedido",
            JSON.stringify(pedido)
        );


        // Feedback visual
        const textoOriginal = botao.innerHTML;

        botao.innerHTML = "✓ Adicionado";
        botao.disabled = true;


        setTimeout(function () {

            botao.innerHTML = textoOriginal;
            botao.disabled = false;

        }, 1000);

    });

});
```

});
