package mock

import com.google.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import repo.GenericMongoRepo

class MockRepo @Inject()(implicit reactiveMongoApi: ReactiveMongoApi)
  extends GenericMongoRepo[MockModel, String]("mock", ModelEvidence.reads, ModelEvidence.writes){

}
