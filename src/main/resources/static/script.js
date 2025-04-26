class TravelAgencyAPI {
    constructor(baseUrl = 'http://localhost:8080') {
        this.baseUrl = baseUrl;
    }

    async _request(endpoint, method = 'GET', body = null) {
        const url = `${this.baseUrl}${endpoint}`;
        const options = {
            method,
            headers: {'Content-Type': 'application/json'},
            body: body ? JSON.stringify(body) : null
        };

        try {
            const response = await fetch(url, options);
            const data = await response.json();

            if (data.message) alert(`✅ ${data.message}`);
            if (data.error) alert(`❌ Erro: ${data.error}`);

            return data;
        } catch (error) {
            alert('⚠️ Erro de conexão com o servidor');
            throw error;
        }
    }

    // Clientes
    async listarClientes() {
        return this._request('/api/v1/clientes/listar');
    }

    async cadastrarCliente(data) {
        return this._request('/api/v1/clientes/cadastrar', 'POST', data);
    }

    async removerCliente(documento) {
        return this._request(`/api/v1/clientes/remover/${documento}`, 'POST');
    }

    async listarPacotesDoCliente(documento) {
        return this._request(`/api/v1/clientes/listar_pacotes/${documento}`);
    }

    // Pacotes
    async listarPacotes() {
        return this._request('/api/v1/pacotes/listar');
    }

    async cadastrarPacote(data) {
        return this._request('/api/v1/pacotes/cadastrar', 'POST', data);
    }

    async removerPacote(id) {
        return this._request(`/api/v1/pacotes/remover/${id}`, 'POST');
    }

    async listarClientesDoPacote(id) {
        return this._request(`/api/v1/pacotes/listar_clientes/${id}`);
    }

    // Serviços
    async listarServicos() {
        return this._request('/api/v1/servicos/listar');
    }

    async cadastrarServico(data) {
        return this._request('/api/v1/servicos/cadastrar', 'POST', data);
    }

    async removerServico(id) {
        return this._request(`/api/v1/servicos/remover/${id}`, 'POST');
    }

    // Pedidos
    async cadastrarPedido(data) {
        return this._request('/api/v1/pedido/cadastrar', 'POST', data);
    }

    async listarPedidos() {
        return this._request('/api/v1/pedido/listar');
    }

    async removerPedido(id) {
        return this._request(`/api/v1/pedido/remover/${id}`, 'POST');
    }
}

class InterfaceController {
    constructor() {
        this.api = new TravelAgencyAPI();
        this.modal = document.getElementById('modal');
        this.initEventos();
        this.carregarDadosIniciais();
    }

    initEventos() {
        // Tabs
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', () => this.mudarTab(btn.dataset.tab));
        });

        // Botões de adição
        document.getElementById('add-client-btn').addEventListener('click', () => this.mostrarFormCliente());
        document.getElementById('add-package-btn').addEventListener('click', () => this.mostrarFormPacote());
        document.getElementById('add-service-btn').addEventListener('click', () => this.mostrarFormServico());
        document.getElementById('add-pedido-btn').addEventListener('click', () => this.mostrarFormPedido());

        // Modal
        document.querySelector('.close-btn').addEventListener('click', () => this.fecharModal());
        window.addEventListener('click', (e) => e.target === this.modal && this.fecharModal());

        // Busca de clientes
        document.getElementById('client-search').addEventListener('input', (e) => this.filtrarClientes(e.target.value));

        document.getElementById('modal-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.enviarFormulario(e);
        });

    }

    async carregarDadosIniciais() {
        await this.carregarClientes();
        await this.carregarPacotes();
        await this.carregarServicos();
        await this.carregarPedidos();
    }


    // Funções para Clientes
    async carregarClientes() {
        const tbody = document.getElementById('clientes-lista');
        tbody.innerHTML = '<tr><td colspan="6" class="loading"><i class="fas fa-spinner"></i> Carregando clientes...</td></tr>';

        try {
            const clientes = await this.api.listarClientes();
            tbody.innerHTML = clientes.length ? clientes.map(cliente => `
                <tr>
                    <td>${cliente.nome}</td>
                    <td>${cliente.telefone}</td>
                    <td>${cliente.email}</td>
                    <td>${cliente.documento || cliente.cpf || cliente.passaporte}</td>
                    <td><span class="badge ${cliente.tipo}">${cliente.tipo === 'NACIONAL' ? 'CPF' : 'Passaporte'}</span></td>                    
                    <td>
                        <button class="icon-btn view" 
                            onclick="interfaceCtrl.mostrarPacotesDoCliente('${cliente.documento || cliente.cpf || cliente.passaporte}')"
                            title="Ver pacotes comprados">
                            <i class="fas fa-suitcase"></i>
                        </button>
                        <button class="icon-btn delete" 
                            onclick="interfaceCtrl.apagarCliente('${cliente.documento || cliente.cpf || cliente.passaporte}')"
                            title="Excluir cliente">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `).join('') : '<tr><td colspan="6" class="empty-state">Nenhum cliente cadastrado</td></tr>';
        } catch (error) {
            tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Erro ao carregar clientes</td></tr>';
        }
    }

    filtrarClientes(termo) {
        const linhas = document.querySelectorAll('#clientes-lista tr');
        termo = termo.toLowerCase();

        linhas.forEach(linha => {
            const texto = linha.textContent.toLowerCase();
            linha.style.display = texto.includes(termo) ? '' : 'none';
        });
    }

    async apagarCliente(documento) {
        if (confirm(`Tem certeza que deseja excluir o cliente ${documento}?`)) {
            await this.api.removerCliente(documento);
            await this.carregarClientes();
        }
    }

    // Funções para Pacotes
    async carregarPacotes() {
        const container = document.getElementById('pacotes-lista');
        container.innerHTML = '<div class="loading"><i class="fas fa-spinner"></i> Carregando pacotes...</div>';

        try {
            const pacotes = await this.api.listarPacotes();
            container.innerHTML = pacotes.length ? pacotes.map(pacote => `
                <div class="package-card">
                    <h3>${pacote.nome}</h3>
                    <p><i class="fas fa-map-marker-alt"></i> ${pacote.destino}</p>
                    <p><i class="fas fa-calendar-alt"></i> ${pacote.duracaoDias} dias</p>
                    <p>${pacote.tipo}</p>
                    <p>${pacote.descricao}</p>
                    <p class="price">R$ ${pacote.preco.toFixed(2).replace('.', ',')}</p>
                   <div class="card-actions">
                        <button class="btn small" onclick="interfaceCtrl.mostrarClientesDoPacote(${pacote.id})"
                            title="Ver clientes que compraram este pacote">
                            <i class="fas fa-users"></i> Clientes
                        </button>
                        <button class="btn small danger" onclick="interfaceCtrl.apagarPacote(${pacote.id})">
                            <i class="fas fa-trash"></i> Excluir
                        </button>
                    </div>
                </div>
            `).join('') : '<div class="empty-state"><i class="fas fa-suitcase"></i><p>Nenhum pacote cadastrado</p></div>';
        } catch (error) {
            container.innerHTML = '<div class="empty-state"><i class="fas fa-exclamation-triangle"></i><p>Erro ao carregar pacotes</p></div>';
        }
    }

    async apagarPacote(id) {
        if (confirm(`Tem certeza que deseja excluir este pacote?`)) {
            await this.api.removerPacote(id);
            await this.carregarPacotes();
        }
    }

    // Funções para Serviços (similar aos pacotes)
    async carregarServicos() {
        const container = document.getElementById('servicos-lista');
        container.innerHTML = '<div class="loading"><i class="fas fa-spinner"></i> Carregando serviços...</div>';

        try {
            const servicos = await this.api.listarServicos();
            container.innerHTML = servicos.length ? servicos.map(servico => `
                <div class="service-card">
                    <h3>${servico.nome}</h3>
                    <p>${servico.descricao || 'Sem descrição'}</p>
                    <p class="price">R$ ${servico.preco?.toFixed(2).replace('.', ',')}</p>
                    <div class="card-actions">
                        <button class="btn small danger" onclick="interfaceCtrl.apagarServico(${servico.id})">
                            Excluir
                        </button>
                    </div>
                </div>
            `).join('') : '<div class="empty-state"><i class="fas fa-concierge-bell"></i><p>Nenhum serviço cadastrado</p></div>';
        } catch (error) {
            container.innerHTML = '<div class="empty-state"><i class="fas fa-exclamation-triangle"></i><p>Erro ao carregar serviços</p></div>';
        }
    }

    async apagarServico(id) {
        if (confirm(`Tem certeza que deseja excluir este serviço?`)) {
            await this.api.removerServico(id);
            await this.carregarServicos();
        }
    }

    async carregarPedidos() {
        const tbody = document.getElementById('pedidos-lista');
        tbody.innerHTML = '<tr><td colspan="6" class="loading"><i class="fas fa-spinner"></i> Carregando pedidos...</td></tr>';

        try {
            const pedidos = await this.api.listarPedidos();
            tbody.innerHTML = pedidos.length ? await Promise.all(pedidos.map(async pedido => {
                let cliente = pedido.cliente;
                let pacote = pedido.pacote;

                let servicos = [];

                for (let servico of pedido.servicos) {
                    servicos.push(`${servico.servico.nome} (${servico.servico.id}) [R$${servico.precoUnitario} x ${servico.quantidade}]`);
                }

                let documento = cliente.tipo === "NACIONAL" ? cliente.cpf : cliente.passaporte;
                return `
                <tr>
                    <td>${cliente.nome} (${documento})</td>
                    <td>${pacote.nome} (${pacote.id}) [RS$${pacote.preco}]</td>
                    <td>${new Date(pedido.dataViagem).toLocaleDateString('pt-BR')}</td>
                    <td>${servicos.join('<br>') || 'Nenhum'}</td>
                    <td>R$ ${pedido.valorTotal}</td>
                    <td>
                        <button class="icon-btn delete" onclick="interfaceCtrl.apagarPedido(${pedido.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
            })).then(html => html.join('')) : '<tr><td colspan="6" class="empty-state">Nenhum pedido encontrado</td></tr>';
        } catch (error) {
            console.log(error);
            tbody.innerHTML = '<tr><td colspan="6" class="empty-state">Erro ao carregar pedidos</td></tr>';
        }
    }

    async apagarPedido(id) {
        if (confirm('Tem certeza que deseja excluir este pedido?')) {
            await this.api.removerPedido(id);
            await this.carregarPedidos();
        }
    }

    // Pedidos

    async mostrarFormPedido() {
        const [clientes, pacotes, servicos] = await Promise.all([
            this.api.listarClientes(),
            this.api.listarPacotes(),
            this.api.listarServicos()
        ]);

        this.mostrarModal('Novo Pedido', [
            {
                label: 'Cliente',
                input: `
                <select id="pedido-cliente" required>
                    ${clientes.map(c => `
                        <option value="${c.documento || c.cpf || c.passaporte}">${c.nome} (${c.documento})</option>
                    `).join('')}
                </select>
            `,
                required: true
            },
            {
                label: 'Pacote',
                input: `
                <select id="pedido-pacote" required>
                    ${pacotes.map(p => `
                        <option value="${p.id}">${p.nome} (${p.destino})</option>
                    `).join('')}
                </select>
            `,
                required: true
            },
            {
                label: 'Data da Viagem',
                input: '<input type="date" id="pedido-data" required>',
                required: true
            },
            {
                label: 'Serviços Adicionais',
                input: `
                <div class="servicos-container">
                    ${servicos.map(s => `
                        <div class="servico-item">
                            <label>
                                <input type="checkbox" value="${s.id}">
                                ${s.nome} (R$ ${s.preco.toFixed(2)})
                            </label>
                            <input type="number" min="1" value="1" class="quantidade" disabled>
                        </div>
                    `).join('')}
                </div>
            `
            }
        ]);

        document.querySelectorAll('.servico-item input[type="checkbox"]').forEach(checkbox => {
            checkbox.addEventListener('change', (e) => {
                const quantInput = e.target.closest('.servico-item').querySelector('.quantidade');
                quantInput.disabled = !e.target.checked;
                if (!e.target.checked) quantInput.value = 1;
            });
        });
    }


    // Funções do Modal
    mostrarModal(titulo, campos) {
        const fieldsContainer = document.getElementById('modal-fields');
        fieldsContainer.innerHTML = campos.map(campo => `
            <label>
                ${campo.label}
                ${campo.input}
                ${campo.required ? '<span class="required">*</span>' : ''}
            </label>
        `).join('');

        document.getElementById('modal-title').textContent = titulo;
        this.modal.style.display = 'block';
    }

    // Add these methods to InterfaceController
    mostrarFormPacote() {
        this.mostrarModal('Cadastrar Novo Pacote', [
            {label: 'Nome', input: '<input name="nome" required>'},
            {label: 'Destino', input: '<input name="destino" required>'},
            {label: 'Duração (dias)', input: '<input type="number" name="duracaoDias" required>'},
            {label: 'Preço', input: '<input type="number" step="0.01" name="preco" required>'},
            {label: 'Tipo', input: '<input name="tipo" required>'},
            {label: 'Descrição', input: '<input name="descricao" required>'}
        ]);
    }

    mostrarFormServico() {
        this.mostrarModal('Cadastrar Novo Serviço', [
            {label: 'Nome', input: '<input name="nome" required>'},
            {label: 'Descrição', input: '<input name="descricao" required>'},
            {label: 'Preço', input: '<input type="number" step="0.01" name="preco" required>'}
        ]);
    }

    async mostrarClientesDoPacote(pacoteId) {
        try {
            const response = await this.api.listarClientesDoPacote(pacoteId);
            const clientes = Array.isArray(response) ? response : [response];
            const lista = clientes.map(c =>
                `• ${c.nome} (${c.documento || c.cpf || c.passaporte})`
            ).join('\n');

            alert(`Clientes neste pacote:\n${lista || 'Nenhum cliente encontrado'}`);
        } catch (error) {
            alert('Erro ao carregar clientes do pacote');
        }
    }

    async mostrarPacotesDoCliente(documento) {
        try {
            const response = await this.api.listarPacotesDoCliente(documento);
            const pacotes = Array.isArray(response) ? response : [response];
            const lista = pacotes.map(p =>
                `• ${p.nome} (${p.destino}) - R$ ${p.preco?.toFixed(2).replace('.', ',')}`
            ).join('\n');

            alert(`Pacotes deste cliente:\n${lista || 'Nenhum pacote encontrado'}`);
        } catch (error) {
            alert('Erro ao carregar pacotes do cliente');
        }
    }


    fecharModal() {
        this.modal.style.display = 'none';
    }

    // Forms
    mostrarFormCliente() {
        this.mostrarModal('Cadastrar Novo Cliente', [
            {
                label: 'Nome Completo',
                input: '<input type="text" name="nome" required>',
                required: true
            },
            {
                label: 'Tipo de Cliente',
                input: `<select name="tipo" required>
                    <option value="NACIONAL">Nacional (CPF)</option>
                    <option value="ESTRANGEIRO">Estrangeiro (Passaporte)</option>
                </select>`,
                required: true
            },
            {
                label: 'Documento',
                input: '<input type="text" name="documento" required>',
                required: true
            },
            {
                label: 'E-mail',
                input: '<input type="email" name="email" required>',
                required: true
            },
            {
                label: 'Telefone',
                input: '<input type="tel" name="telefone" placeholder="+99 (99) 99999-9999" required>',
                required: true
            }
        ]);
    }

    async enviarFormulario(e, campos) {
        e.preventDefault();
        const formData = Object.fromEntries(new FormData(e.target));

        const modalTitle = document.getElementById('modal-title').textContent;

        try {
            if (document.activeElement.closest('.modal-content')) {

                if (modalTitle.includes('Cliente')) {
                    await this.api.cadastrarCliente({
                        ...formData,
                    });
                    await this.carregarClientes();
                } else if (modalTitle.includes('Pacote')) {
                    await this.api.cadastrarPacote({
                        ...formData,
                        preco: parseFloat(formData.preco),
                        duracao: parseInt(formData.duracao)
                    });
                    await this.carregarPacotes();
                } else if (modalTitle.includes('Serviço')) {
                    await this.api.cadastrarServico({
                        ...formData,
                        preco: parseFloat(formData.preco)
                    });
                    await this.carregarServicos();
                } else if (modalTitle.includes('Pedido')) {
                    const servicos = Array.from(document.querySelectorAll('.servico-item input[type="checkbox"]:checked'))
                        .reduce((acc, checkbox) => {
                            const servicoItem = checkbox.closest('.servico-item');
                            const quantidadeInput = servicoItem.querySelector('.quantidade');
                            const servicoId = checkbox.value;
                            const quantidade = parseInt(quantidadeInput.value) || 0;

                            if (quantidade > 0) {
                                acc[servicoId] = quantidade;
                            }
                            return acc;
                        }, {});

                    await this.api.cadastrarPedido({
                        clienteDocumento: document.getElementById('pedido-cliente').value,
                        pacoteId: parseInt(document.getElementById('pedido-pacote').value),
                        dataViagem: document.getElementById('pedido-data').value,
                        servicosAdicionaisIdQuant: servicos
                    });
                    await this.carregarPedidos();
                }
            }
            this.fecharModal();
        } catch (error) {
            console.error('Erro ao enviar formulário:', error);
        }
    }

    mudarTab(tab) {
        document.querySelectorAll('.tab-content, .tab-btn').forEach(el => {
            el.classList.remove('active');
        });
        document.getElementById(tab).classList.add('active');
        document.querySelector(`[data-tab="${tab}"]`).classList.add('active');
    }
}

// Inicialização
const interfaceCtrl = new InterfaceController();
