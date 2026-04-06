const API_URL = "http://localhost:8080/products";
let editingProductId = null;

async function loadProducts() {
    const response = await fetch(API_URL);
    const products = await response.json();

    const list = document.getElementById("productList");
    list.innerHTML = "";

    products.forEach(product => {
        const li = document.createElement("li");
        li.className = "d-flex justify-content-between align-items-center bg-white border rounded p-3";
        li.innerHTML = `
            <span class="product-info">${product.name} | R$ ${product.price} | Qtd: ${product.quantity}</span>
            <div class="d-flex gap-2">
                <button class="btn btn-outline-warning btn-sm" onclick="editProduct(${product.id})">Editar</button>
                <button class="btn btn-outline-danger btn-sm" onclick="deleteProduct(${product.id})">Excluir</button>
            </div>
    `;
        list.appendChild(li);
    });
}

async function addProduct() {
    const name = document.getElementById("name").value;
    const price = document.getElementById("price").value;
    const quantity = document.getElementById("quantity").value;

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: name,
                price: parseFloat(price),
                quantity: parseInt(quantity)
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            alert("Erro: " + errorText);
            return;
        }

        clearForm();
        loadProducts();
    } catch (error) {
        alert("Erro de conexão: " + error.message);
    }
}

function editProduct(id) {
    editingProductId = id;

    fetch(`${API_URL}/${id}`).then(response => response.json()).then(product => {
            document.getElementById("name").value = product.name;
            document.getElementById("price").value = product.price;
            document.getElementById("quantity").value = product.quantity;
        });
}

async function updateProduct() {
    const name = document.getElementById("name").value;
    const price = document.getElementById("price").value;
    const quantity = document.getElementById("quantity").value;

    try {
        const response = await fetch(`${API_URL}/${editingProductId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: name,
                price: parseFloat(price),
                quantity: parseInt(quantity)
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            alert("Erro: " + errorText);
            return;
        }

        editingProductId = null;
        clearForm();
        loadProducts();
    } catch (error) {
        alert("Erro de conexão: " + error.message);
    }
}

async function deleteProduct(id) {
    await fetch(`${API_URL}/${id}`, {
        method: "DELETE"
    });

    loadProducts();
}

function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("price").value = "";
    document.getElementById("quantity").value = "";
}

loadProducts();
