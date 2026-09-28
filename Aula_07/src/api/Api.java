package api;

import io.javalin.Javalin;

import java.util.List;

import model.Projeto;
import service.ProjetoService;

public class Api {

    public static void main(String[] args) {

        ProjetoService service =
            new ProjetoService();

        var app = Javalin.create(config -> {

            // Rota inicial
            config.routes.get("/", ctx -> {

                ctx.result(
                    "API Sistema de Projetos"
                );

            });

            // Listar projetos
            config.routes.get(
                "/api/projetos",
                ctx -> {

                    List<Projeto> projetos =
                        service.listar();

                    ctx.json(projetos);
                }
            );

            // Buscar por ID
            config.routes.get(
                "/api/projetos/{id}",
                ctx -> {

                    int id = Integer.parseInt(
                        ctx.pathParam("id")
                    );

                    Projeto projeto =
                        service.buscarPorId(id);

                    if (projeto == null) {

                        ctx.status(404);

                        return;
                    }

                    ctx.json(projeto);
                }
            );

        }).start(7070);
    }
}