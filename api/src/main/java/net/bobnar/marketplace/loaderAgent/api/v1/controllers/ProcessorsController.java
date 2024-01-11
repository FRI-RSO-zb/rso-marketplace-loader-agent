package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.common.dtos.loaderAgent.v1.processors.ProcessingResult;
import net.bobnar.marketplace.loaderAgent.api.v1.dtos.DebugConfig;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.avtonet.AvtoNetProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiProcessor;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonProcessor;
//import org.eclipse.microprofile.metrics.Meter;
//import org.eclipse.microprofile.metrics.annotation.Metered;
//import org.eclipse.microprofile.metrics.annotation.Metric;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Log
@Path("processors")
@Tag(name="Processors", description = "Endpoints for triggering processing of obtained raw data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(name="processors", allowOrigin = "*", supportedMethods = "GET, HEAD, POST, OPTIONS, DELETE")
public class ProcessorsController {
    private Logger log = Logger.getLogger(ProcessorsController.class.getName());

    private static final String AVTONET = "avtonet";
    private static final String BOLHA = "bolha";
    private static final String DOBERAVTO = "doberavto";
    private static final String SALOMON = "salomon";
    private static final String OGLASISI = "oglasisi";

    private static final String LIST_TYPE = "list";

//    @Inject
//    @Metric(name="processing_meter")
//    private Meter processingMeter;

    @GET
    @Operation(
            summary = "Get the list of processors",
            description = "Returns the list of supported processor modules."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of processors",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))
            )
    })
    public Response getListOfProcessors() {
        List<String> list = new ArrayList<>();
        list.add(AVTONET);
        list.add(BOLHA);
        list.add(DOBERAVTO);
        list.add(SALOMON);
        list.add(OGLASISI);

        return Response.ok(list)
                .build();
    }

    @GET
    @Path("{processor}/types")
    @Operation(
            summary = "Get the list of input types in processor",
            description = "Returns the list of supported source input types by the processor module."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of supported input types",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = String.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Processor module not found."
            ),
    })
    public Response getListOfLoaderPages(
            @PathParam("processor") @Parameter(description = "The name of processor", example = AVTONET, required = true) String processor
    ) {
        if (processor.equals(AVTONET) || processor.equals(BOLHA) || processor.equals(DOBERAVTO) || processor.equals(SALOMON) || processor.equals(OGLASISI)) {
            List<String> list = new ArrayList<>();
            list.add(LIST_TYPE);

            return Response.ok(list).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{processor}/{type}/process")
    @Operation(
            summary = "Processes the input",
            description = "Start the processing job for the input using specified processor."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Input processed",
                    content = @Content(schema = @Schema(implementation = ProcessingResult.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Input type not supported by processor."
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Input processing failed."
            )
    })
//    @Timed(name="processors_process_timer")
//    @Metered(name="processors_process_meter")
    public Response process(
            @PathParam("processor") @Parameter(description = "The name of processor", example = AVTONET, required = true) String processor,
            @PathParam("type") @Parameter(description = "The type of input data", example = LIST_TYPE, required = true) String type,
            @RequestBody(description = "The input data to be processed", content = @Content(example = SAMPLE_AVTONET_PAGE), required = true) String data
    ) {
//        this.processingMeter.mark();

        ProcessingResult result = switch (processor) {
            case AVTONET -> switch (type) {
                case LIST_TYPE -> new AvtoNetProcessor().processItemList(data).toDto();
                default -> null;
            };
            case BOLHA -> switch (type) {
                case LIST_TYPE -> new BolhaProcessor().processItemList(data).toDto();
                default -> null;
            };
            case DOBERAVTO -> switch (type) {
                case LIST_TYPE -> new DoberAvtoProcessor().processItemList(data).toDto();
                default -> null;
            };
            case SALOMON -> switch (type) {
                case LIST_TYPE -> new SalomonProcessor().processItemList(data).toDto();
                default -> null;
            };
            case OGLASISI -> switch (type) {
                case LIST_TYPE -> new OglasiSiProcessor().processItemList(data).toDto();
                default -> null;
            };
            default -> null;
        };

        return result != null ?
                Response.ok(result).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    private static final String SAMPLE_AVTONET_PAGE = """
            <!DOCTYPE html>
            <html>
                        
            <head>
                <title>www.Avto.net: Zadnjih 100</title>
                <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
            </head>
                        
            <body>
                <div class="col-12 col-lg-9">
                    <form name="results" id="results" action="results.asp" method="post" onsubmit="return checkCheckBox(this)">
                        <input type="hidden" name="vsehzadetkov" value="100">
                        <div class="col-12 pb-3 position-relative w-100">
                        </div>
                        <div class="row bg-white position-relative GO-Results-Row GO-Shadow-B" style="z-index:1">
                            <a class="stretched-link"
                                href="../Ads/details.asp?id=19044636&amp;display=Mercedes-Benz%20C-Razred"></a>
                            <div
                                class="GO-Results-Naziv bg-dark px-3 py-2 font-weight-bold text-truncate text-white text-decoration-none">
                                <span>Mercedes-Benz C-Razred C 220 d AMG Line AUT. FULL LED USNJE ALU18
                                    KAMERA</span>
                            </div>
                            <div class="GO-Results-NazivPark bg-dark px-3 py-2 text-white text-decoration-none">
                                <span class="text-right text-white" style="z-index: 1; position: relative">
                                    <a href="#" data-toggle="modal" data-target="#ParkiranoModal0">
                                        <i class="fa fa-lg fa-star-o pl-3 text-white" aria-hidden="true"></i>
                                    </a>
                                </span>
                            </div>
                            <div class="col-auto p-3 GO-Results-Photo">
                                <div> <a href="../Ads/details.asp?id=19044636"> <img
                                            src="https://images.avto.net/photo/19044636/1079705_160.jpg"
                                            alt="Mercedes-Benz C-Razred C 220 d AMG Line AUT. FULL LED USNJE ALU18 KAMERA"
                                            title="Mercedes-Benz C-Razred C 220 d AMG Line AUT. FULL LED USNJE ALU18 KAMERA"> </a>
                                </div>
                                <div class="GO-Results-Photo-HD">HD</div>
                            </div>
                            <div class="col-auto text-truncate py-3 GO-Results-Data">
                                <div class="GO-Results-Data-Top">
                                    <table class="table table-striped table-sm table-borderless font-weight-normal mb-0">
                                        <tbody>
                                            <tr>
                                                <td class="w-25 d-none d-md-block pl-3">1.registracija</td>
                                                <td class="w-75 pl-3">2017</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Prevoženih</td>
                                                <td class="pl-3">128000 km</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Gorivo</td>
                                                <td class="pl-3">diesel motor</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Menjalnik</td>
                                                <td class="pl-3 text-truncate">avtomatski menjalnik</td>
                                            </tr>
                                            <tr class="d-none d-md-table-row">
                                                <td class="d-none d-md-block pl-3">Motor</td>
                                                <td class="pl-3 text-truncate">
                                                    2143 ccm, 125 kW / 170 KM
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="d-sm-none col pl-3 pr-3 px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Logo mr-3 ml-sm-0">
                                    <a
                                        href="results321_makes.asp?broker=11386&amp;oglasrubrika=1&amp;karambolirano=1&amp;KAT=1010000000">
                                        <img src="https://www.avto.net/2004/logotipi/11386.gif?koda=6.12.2023 22:10:11" border="0">
                                    </a>
                                </div>
                                <div class="GO-Results-Price mt-0 mt-sm-3 GO-Results-Price-Akcija">
                                    <div class="GO-Results-Price-Akcija-TXT">
                                        AKCIJSKA CENA </div>
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-StaraCena">28.880 €</div>
                                        <div class="GO-Results-Price-TXT-AkcijaCena">27.880 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                            </div>
                            <div class="d-none d-sm-block col-auto px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Price mt-0 mt-sm-3 GO-Results-Price-Akcija">
                                    <div class="GO-Results-Price-Akcija-TXT">
                                        AKCIJSKA CENA </div>
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-StaraCena">28.880 €</div>
                                        <div class="GO-Results-Price-TXT-AkcijaCena">27.880 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                                <div class="GO-Results-Logo ml-3 ml-sm-0">
                                    <a
                                        href="results321_makes.asp?broker=11386&amp;oglasrubrika=1&amp;karambolirano=1&amp;KAT=1010000000">
                                        <img src="https://www.avto.net/2004/logotipi/11386.gif?koda=6.12.2023 22:10:11" border="0">
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 pb-3 position-relative w-100">
                        </div>
                        <div class="row bg-white position-relative GO-Results-Row GO-Shadow-B" style="z-index:1">
                            <a class="stretched-link" href="../Ads/details.asp?id=19044635&amp;display=Audi%20RS3"></a>
                            <div
                                class="GO-Results-Naziv bg-dark px-3 py-2 font-weight-bold text-truncate text-white text-decoration-none">
                                <span>Audi RS3 </span>
                            </div>
                            <div class="GO-Results-NazivPark bg-dark px-3 py-2 text-white text-decoration-none">
                                <span class="text-right text-white" style="z-index: 1; position: relative"> <a href="#"
                                        data-toggle="modal" data-target="#ParkiranoModal0"> <i
                                            class="fa fa-lg fa-star-o pl-3 text-white" aria-hidden="true"></i> </a> </span>
                            </div>
                            <div class="col-auto p-3 GO-Results-Photo">
                                <div> <a href="../Ads/details.asp?id=19044635"> <img
                                            src="https://images.avto.net/photo/19044635/1079739_160.jpg" alt="Audi RS3 "
                                            title="Audi RS3 "> </a>
                                </div>
                            </div>
                            <div class="col-auto text-truncate py-3 GO-Results-Data">
                                <div class="GO-Results-Data-Top">
                                    <table class="table table-striped table-sm table-borderless font-weight-normal mb-0">
                                        <tbody>
                                            <tr>
                                                <td class="w-25 d-none d-md-block pl-3">1.registracija</td>
                                                <td class="w-75 pl-3">2013</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Prevoženih</td>
                                                <td class="pl-3">236000 km</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Gorivo</td>
                                                <td class="pl-3">bencinski motor</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Menjalnik</td>
                                                <td class="pl-3 text-truncate">avtomatski menjalnik</td>
                                            </tr>
                                            <tr class="d-none d-md-table-row">
                                                <td class="d-none d-md-block pl-3">Motor</td>
                                                <td class="pl-3 text-truncate">
                                                    2480 ccm, 250 kW / 340 KM
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="d-sm-none col pl-3 pr-3 px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Logo mr-3 ml-sm-0 border-0">
                                    <img src="../_graphics/rounded/blank.gif" width="120" height="60" border="0">
                                </div>
                                <div class="GO-Results-Price mt-0 mt-sm-3">
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-Regular">21.000 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                            </div>
                            <div class="d-none d-sm-block col-auto px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Price mt-0 mt-sm-3">
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-Regular">21.000 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                                <div class="GO-Results-Logo border-0">
                                    <img src="../_graphics/rounded/blank.gif" width="120" height="60" border="0">
                                </div>
                            </div>
                        </div>
                        <div class="col-12 pb-3 position-relative w-100">
                        </div>
                        <div class="row bg-white position-relative GO-Results-Row GO-Shadow-B" style="z-index:1">
                            <a class="stretched-link" href="../Ads/details.asp?id=19044633&amp;display=Fiat%20500"></a>
                            <div
                                class="GO-Results-Naziv bg-dark px-3 py-2 font-weight-bold text-truncate text-white text-decoration-none">
                                <span>Fiat 500 1.3 Multijet 16v -ODLIČEN -SERVISIRAN -SLOVENSKI </span>
                            </div>
                            <div class="GO-Results-NazivPark bg-dark px-3 py-2 text-white text-decoration-none">
                                <span class="text-right text-white" style="z-index: 1; position: relative"> <a href="#"
                                        data-toggle="modal" data-target="#ParkiranoModal0"> <i
                                            class="fa fa-lg fa-star-o pl-3 text-white" aria-hidden="true"></i> </a> </span>
                            </div>
                            <div class="col-auto p-3 GO-Results-Photo">
                                <div> <a href="../Ads/details.asp?id=19044633"> <img
                                            src="https://images.avto.net/photo/19044633/1079273_160.jpg"
                                            alt="Fiat 500 1.3 Multijet 16v -ODLIČEN -SERVISIRAN -SLOVENSKI "
                                            title="Fiat 500 1.3 Multijet 16v -ODLIČEN -SERVISIRAN -SLOVENSKI "> </a>
                                </div>
                            </div>
                            <div class="col-auto text-truncate py-3 GO-Results-Data">
                                <div class="GO-Results-Data-Top">
                                    <table class="table table-striped table-sm table-borderless font-weight-normal mb-0">
                                        <tbody>
                                            <tr>
                                                <td class="w-25 d-none d-md-block pl-3">1.registracija</td>
                                                <td class="w-75 pl-3">2008</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Gorivo</td>
                                                <td class="pl-3">diesel motor</td>
                                            </tr>
                                            <tr>
                                                <td class="d-none d-md-block pl-3">Menjalnik</td>
                                                <td class="pl-3 text-truncate">ročni menjalnik</td>
                                            </tr>
                                            <tr class="d-none d-md-table-row">
                                                <td class="d-none d-md-block pl-3">Motor</td>
                                                <td class="pl-3 text-truncate">
                                                    1248 ccm, 55 kW / 75 KM
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="d-sm-none col pl-3 pr-3 px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Logo mr-3 ml-sm-0">
                                    <a
                                        href="results321_makes.asp?broker=19855&amp;oglasrubrika=1&amp;karambolirano=1&amp;KAT=1010000000">
                                        <img src="https://www.avto.net/2004/logotipi/19855.gif?koda=6.12.2023 22:10:11" border="0">
                                    </a>
                                </div>
                                <div class="GO-Results-Price mt-0 mt-sm-3">
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-Regular">4.390 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                            </div>
                            <div class="d-none d-sm-block col-auto px-sm-0 pb-sm-3 GO-Results-PriceLogo">
                                <div class="GO-Results-Price mt-0 mt-sm-3">
                                    <div class="GO-Results-Price-Mid">
                                        <div class="GO-Results-Price-TXT-Regular">4.390 €</div>
                                    </div>
                                    <div class="GO-Results-Price-Bottom">
                                    </div>
                                </div>
                                <div class="GO-Results-Logo ml-3 ml-sm-0">
                                    <a
                                        href="results321_makes.asp?broker=19855&amp;oglasrubrika=1&amp;karambolirano=1&amp;KAT=1010000000">
                                        <img src="https://www.avto.net/2004/logotipi/19855.gif?koda=6.12.2023 22:10:11" border="0">
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 pb-3 position-relative w-100">
                        </div>
                    </form>
                </div>
            </body>
                        
            </html>""";
}
