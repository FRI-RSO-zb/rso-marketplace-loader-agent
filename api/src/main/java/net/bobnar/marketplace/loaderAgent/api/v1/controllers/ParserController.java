//package net.bobnar.marketplace.loaderAgent.api.v1.controllers;
//
//import com.kumuluz.ee.logs.cdi.Log;
//import net.bobnar.marketplace.loaderAgent.loaderModules.avtonet.AvtoNetProcessor;
//import net.bobnar.marketplace.loaderAgent.loaderModules.bolha.BolhaProcessor;
//import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
//import org.eclipse.microprofile.openapi.annotations.tags.Tag;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//
//@Log
//@Path("parser")
//@Tag(name="Parser", description = "Endpoints for triggering parsing of obtained raw data")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class ParserController {
//
//
//    @POST
//    @Path("{site}/{type}")
//    public Response parseData(@PathParam("site") String site, @PathParam("type") String type, @RequestBody() String data) {
//        if (site.equals("avtonet")) {
//            if (type.equals("list")) {
//                return Response.ok(new AvtoNetProcessor().processItemList(data)).build();
//            } else if (type.equals("listing")) {
//                return Response.ok(new AvtoNetProcessor().processItem(data)).build();
//            }
//        } else if (site.equals("bolha")) {
//            if (type.equals("list")) {
//                return Response.ok(new BolhaProcessor().processItemList(data)).build();
//            } else if (type.equals("listing")) {
//                return Response.ok(new BolhaProcessor().processItem(data)).build();
//            }
//        }
//
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }
//}
