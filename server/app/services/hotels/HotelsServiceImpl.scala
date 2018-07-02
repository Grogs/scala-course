package services.hotels

import javax.inject.Inject

import model.Hotel

class HotelsServiceImpl @Inject()(catalogueService: HotelCatalogueService,
                              geographyService: GeographyService,
                              hotelFinderService: HotelFinderService) extends HotelsService {

  def search(destination: String, radius: Double): Seq[Hotel] = {
    geographyService
      .lookupDestination(destination)
      .toSeq
      .flatMap(coordinates =>
        hotelFinderService.findHotels(coordinates, radius))
          .flatMap(id => catalogueService.lookupHotel(id))

    for {
      coordinates <- geographyService.lookupDestination(destination).toSeq
      hotelId <- hotelFinderService.findHotels(coordinates, radius)
      hotel <- catalogueService.lookupHotel(hotelId)
    } yield hotel
  }

}
