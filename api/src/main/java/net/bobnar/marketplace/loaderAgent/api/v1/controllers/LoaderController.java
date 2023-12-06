//package net.bobnar.marketplace.loaderAgent.api.v1.controllers;
//
//import com.kumuluz.ee.logs.cdi.Log;
//import net.bobnar.marketplace.loaderAgent.loaderModules.doberAvto.DoberAvtoLoader;
//import net.bobnar.marketplace.loaderAgent.loaderModules.avtonet.AvtoNetLoader;
//import net.bobnar.marketplace.loaderAgent.loaderModules.bolha.BolhaLoader;
//import net.bobnar.marketplace.loaderAgent.loaderModules.oglasiSi.OglasiSiLoader;
//import net.bobnar.marketplace.loaderAgent.loaderModules.salomon.SalomonLoader;
//import org.eclipse.microprofile.openapi.annotations.tags.Tag;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.IOException;
//
//
//@Log
//@Path("loader")
//@Tag(name="Loader", description="Endpoints for starting and reviewing the data loading jobs")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class LoaderController {
//
//    @GET
//    public Response getLoaderJobs() {
//        return Response.ok("0").build();
//    }
//
//
//    @POST
//    @Path("load/{site}/{page}")
//    public Response loadPage(@PathParam("site") String site, @PathParam("page") String page) throws IOException {
//        if (site.equals("avtonet")) {
//            if (page.equals("top100")) {
//                return Response.ok(new AvtoNetLoader().loadAvtonetTop100List()).build();
//            }
//        } else if (site.equals("bolha")) {
//            if (page.equals("latestCars")) {
//                return Response.ok(new BolhaLoader().loadLatestCarAds()).build();
//            }
//        } else if (site.equals("doberavto")) {
//            if (page.equals("latest")) {
//                return Response.ok(new DoberAvtoLoader().loadLatestCarAds()).build();
//            }
//        } else if (site.equals("salomon")) {
//            if (page.equals("latest")) {
//                return Response.ok(new SalomonLoader().loadLatestCarAds()).build();
//            }
//        } else if (site.equals("oglasisi")) {
//            if (page.equals("latest")) {
//                return Response.ok(new OglasiSiLoader().loadLatestCarAds()).build();
//            }
//        }
//
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }
//}
