document.addEventListener("DOMContentLoaded", function () {

```
const formPedido = document.getElementById("formPedido");
const produtosPedido = document.getElementById("produtosPedido");
const listaResumo = document.getElementById("listaResumo");
const valorSubtotal = document.getElementById("valorSubtotal");
const valorTotal = document.getElementById("valorTotal");
const btnConfirmar = document.getElementById("btnConfirmarPedido");
const mensagem = document.getElementById("mensagemConfirmacao");

const campoMesa = document.getElementById("campoMesa");
const campoEntrega = document.getElementById("campoEntrega");

const numeroMesa = document.getElementById("numeroMesa");
const enderecoEntrega = document.getElementById("enderecoEntrega");

let pedido = JSON.parse(localStorage.getItem("pedido")) || [];


// =====================================================
// FORMATAR PREÇO
// =====================================================

function formatarPreco(valor) {
    return Number(valor).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}


// =====================================================
// SALVAR PEDIDO
// =====================================================

function salvarPedido() {
    localStorage.setItem("pedido", JSON.stringify(pedido));
}


// =====================================================
// RENDERIZAR PRODUTOS
// =====================================================

function renderizarProdutos() {

    if (!produtosPedido) {
        return;
    }

    if (pedido.length === 0) {

        produtosPedido.innerHTML = `
          
        `;

        return;
    }

    produtosPedido.innerHTML = "";

    pedido.forEach(function (produto, index) {

        const card = document.createElement("div");

        card.className = "produto-pedido-item";

        card.innerHTML = `
            

            

        
        `;

        produtosPedido.appendChild(card);
    });

    adicionarEventosQuantidade();
}


// =====================================================
// BOTÕES DE QUANTIDADE
// =====================================================

function adicionarEventosQuantidade() {

    const botoesQuantidade =
        document.querySelectorAll(".btn-quantidade");

    botoesQuantidade.forEach(function (botao) {

        botao.addEventListener("click", function () {

            const index = Number(botao.dataset.index);
            const acao = botao.dataset.acao;

            if (!pedido[index]) {
                return;
            }

            if (acao === "aumentar") {

                if (pedido[index].quantidade < 10) {
                    pedido[index].quantidade++;
                }

            }

            if (acao === "diminuir") {

                pedido[index].quantidade--;

                if (pedido[index].quantidade <= 0) {
                    pedido.splice(index, 1);
                }
            }

            salvarPedido();
            atualizarTela();
        });
    });


    const botoesRemover =
        document.querySelectorAll(".btn-remover");

    botoesRemover.forEach(function (botao) {

        botao.addEventListener("click", function () {

            const index = Number(botao.dataset.index);

            pedido.splice(index, 1);

            salvarPedido();
            atualizarTela();
        });
    });
}


// =====================================================
// ATUALIZAR RESUMO
// =====================================================

function atualizarResumo() {

    if (!listaResumo) {
        return;
    }

    listaResumo.innerHTML = "";

    let subtotal = 0;

    if (pedido.length === 0) {

        listaResumo.innerHTML = `
         
        `;

    } else {

        pedido.forEach(function (produto) {

            const valorProduto =
                Number(produto.preco) *
                Number(produto.quantidade);

            subtotal += valorProduto;

            const item =
                document.createElement("div");

            item.className = "summary-item";

            item.innerHTML = `
                
            `;

            listaResumo.appendChild(item);
        });
    }

    if (valorSubtotal) {
        valorSubtotal.textContent =
            formatarPreco(subtotal);
    }

    if (valorTotal) {
        valorTotal.textContent =
            formatarPreco(subtotal);
    }
}


// =====================================================
// TIPO DE ATENDIMENTO
// =====================================================

const opcoesAtendimento =
    document.querySelectorAll(
        'input[name="tipoAtendimento"]'
    );


function atualizarAtendimento() {

    const selecionado =
        document.querySelector(
            'input[name="tipoAtendimento"]:checked'
        );


    if (campoMesa) {
        campoMesa.style.display = "none";
    }

    if (campoEntrega) {
        campoEntrega.style.display = "none";
    }


    if (numeroMesa) {
        numeroMesa.required = false;
    }

    if (enderecoEntrega) {
        enderecoEntrega.required = false;
    }


    if (!selecionado) {
        atualizarBotaoConfirmar();
        return;
    }


    if (selecionado.value === "MESA") {

        if (campoMesa) {
            campoMesa.style.display = "block";
        }

        if (numeroMesa) {
            numeroMesa.required = true;
        }
    }


    if (selecionado.value === "ENTREGA") {

        if (campoEntrega) {
            campoEntrega.style.display = "block";
        }

        if (enderecoEntrega) {
            enderecoEntrega.required = true;
        }
    }

    atualizarBotaoConfirmar();
}


opcoesAtendimento.forEach(function (opcao) {

    opcao.addEventListener(
        "change",
        atualizarAtendimento
    );

});


// =====================================================
// HABILITAR / DESABILITAR BOTÃO
// =====================================================

function atualizarBotaoConfirmar() {

    if (!btnConfirmar) {
        return;
    }

    const selecionado =
        document.querySelector(
            'input[name="tipoAtendimento"]:checked'
        );

    let valido = pedido.length > 0;


    if (!selecionado) {
        valido = false;
    }


    if (
        selecionado &&
        selecionado.value === "MESA"
    ) {

        if (
            !numeroMesa ||
            numeroMesa.value === ""
        ) {
            valido = false;
        }
    }


    if (
        selecionado &&
        selecionado.value === "ENTREGA"
    ) {

        if (
            !enderecoEntrega ||
            enderecoEntrega.value.trim() === ""
        ) {
            valido = false;
        }
    }


    btnConfirmar.disabled = !valido;
}


// =====================================================
// EVENTOS DOS CAMPOS
// =====================================================

if (numeroMesa) {

    numeroMesa.addEventListener(
        "change",
        atualizarBotaoConfirmar
    );

}


if (enderecoEntrega) {

    enderecoEntrega.addEventListener(
        "input",
        atualizarBotaoConfirmar
    );

}


// =====================================================
// ENVIAR PEDIDO
// =====================================================

if (formPedido) {

    formPedido.addEventListener(
        "submit",
        function (evento) {

            if (pedido.length === 0) {

                evento.preventDefault();

                mostrarMensagem(
                    "Selecione pelo menos um produto.",
                    "erro"
                );

                return;
            }


            const selecionado =
                document.querySelector(
                    'input[name="tipoAtendimento"]:checked'
                );


            if (!selecionado) {

                evento.preventDefault();

                mostrarMensagem(
                    "Selecione o tipo de atendimento.",
                    "erro"
                );

                return;
            }


            if (
                selecionado.value === "MESA" &&
                (
                    !numeroMesa ||
                    numeroMesa.value === ""
                )
            ) {

                evento.preventDefault();

                mostrarMensagem(
                    "Selecione o número da mesa.",
                    "erro"
                );

                return;
            }


            if (
                selecionado.value === "ENTREGA" &&
                (
                    !enderecoEntrega ||
                    enderecoEntrega.value.trim() === ""
                )
            ) {

                evento.preventDefault();

                mostrarMensagem(
                    "Digite o endereço para entrega.",
                    "erro"
                );

                return;
            }


            // Remove campos antigos
            document
                .querySelectorAll(".produto-hidden")
                .forEach(function (campo) {
                    campo.remove();
                });


            
// =====================================================
// MENSAGEM
// =====================================================

function mostrarMensagem(texto, tipo) {

    if (!mensagem) {
        return;
    }

    mensagem.textContent = texto;

    mensagem.classList.remove(
        "mensagem-sucesso",
        "mensagem-erro"
    );


    if (tipo === "sucesso") {

        mensagem.classList.add(
            "mensagem-sucesso"
        );

    } else {

        mensagem.classList.add(
            "mensagem-erro"
        );
    }


    mensagem.scrollIntoView({
        behavior: "smooth",
        block: "nearest"
    });
}


// =====================================================
// ATUALIZAR TELA
// =====================================================

function atualizarTela() {

    renderizarProdutos();
    atualizarResumo();
    atualizarBotaoConfirmar();
}


// =====================================================
// INICIALIZAÇÃO
// =====================================================

atualizarAtendimento();
atualizarTela();
```

});
