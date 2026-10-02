package api;

import io.javalin.Javalin;
import java.util.List;
import model.Projeto;
import service.ProjetoService;

public class Api {

    public static void main(String[] args) throws Exception {

        ProjetoService service = new ProjetoService();
        service.carregar();

        // Inicializa o Javalin na porta 7070
        var app = Javalin.create().start(7070);

        // =====================================
        // ROTA INICIAL
        // =====================================
        app.get("/", ctx -> {
            ctx.result("API Sistema de Projetos");
        });

        // =====================================
        // GET - LISTAR
        // =====================================
        app.get("/api/projetos", ctx -> {
            List<Projeto> projetos = service.listar();
            ctx.json(projetos); 
        });

        // =====================================
        // GET - BUSCAR POR ID
        // =====================================
        app.get("/api/projetos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Projeto projeto = service.buscarPorId(id);

            if (projeto == null) {
                ctx.status(404);
                return;
            }

            ctx.json(projeto);
        });

                    // =====================================
            // POST - CADASTRAR
            // =====================================

            app.post("/api/projetos", ctx -> {

                    Projeto projeto =
                        ctx.bodyAsClass(
                            Projeto.class
                        );

                    if (
                        projeto.getNome() == null ||
                        projeto.getNome().isBlank()
                    ) {

                        ctx.status(400);

                        ctx.json(
                            new ErroResponse(
                                "Nome é obrigatório"
                            )
                        );

                        return;
                    }

                    projeto.setId(
                        service.proximoId()
                    );

                    service.adicionar(projeto);

                    service.salvar();

                    ctx.status(201);

                    ctx.json(projeto);
                }
            );


        // =====================================
        // PUT - ALTERAR
        // =====================================
        app.put("/api/projetos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Projeto projeto = ctx.bodyAsClass(Projeto.class);

            projeto.setId(id);
            boolean alterou = service.alterar(projeto);

            if (!alterou) {
                ctx.status(404);
                return;
            }

            service.salvar();
            ctx.json(projeto);
        });

        // =====================================
        // DELETE - EXCLUIR
        // =====================================
        app.delete("/api/projetos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Projeto projeto = service.buscarPorId(id);

            if (projeto == null) {
                ctx.status(404);
                return;
            }

            service.removerPorId(id);
            service.salvar();
            ctx.status(204);
        });
    }

    // Estrutura simples para gerar a resposta de erro como {"mensagem": "..."}
    private record ErroResponse(String mensagem) {
    }
}

