package example

import com.google.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import repo.GenericMongoRepo

class RoleRepo @Inject()(implicit reactiveMongoApi: ReactiveMongoApi) extends GenericMongoRepo[Role, Int]("roles", RoleMongoFormat.reads, RoleMongoFormat.writes)
