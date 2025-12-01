
# JavaFX + FXML + MySQL (Maven) — CRUD de Alunos

Aplicação completa para aula com **telas em FXML** (lista + formulário + sobre), **DAO** com JDBC e **MySQL**.

## Pré-requisitos
- **JDK 17+**
- **MySQL** local
- **Maven 3.9+** (ou o Maven embutido da IDE)

## Banco de dados
Crie o banco uma vez:
```sql
CREATE DATABASE escola CHARACTER SET utf8mb4;
```
Depois edite `src/main/resources/com/example/fxmysqlfxml/config.properties` com usuário/senha.

> Na primeira execução, a tabela `students` é criada automaticamente.

## Rodando
No terminal, dentro da pasta do projeto:
```bash
mvn -P runfx javafx:run
```
Na IDE, execute o goal **javafx:run**.

## Telas
- **MainView.fxml** — tabela de alunos e botões *Novo, Editar, Excluir, Atualizar*.
- **StudentForm.fxml** — formulário modal para criar/editar.
- **AboutView.fxml** — janela "Sobre".

## Estrutura Java
- `App` — carrega a tela principal.
- `MainController` — ações da tela principal e abertura dos diálogos.
- `StudentFormController` — validação e retorno do aluno digitado.
- `AboutController` — texto estático.
- `Db` — lê `config.properties`, abre conexão e cria tabela.
- `StudentDao` — CRUD com `PreparedStatement`.
- `Student` — entidade.

## Ideias de exercícios
- Adicionar filtro por nome no topo da MainView.
- Validar e-mail com regex.
- Paginação (LIMIT/OFFSET) no DAO.
- Exportar/Importar CSV.

Bom estudo!
