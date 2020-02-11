package com.prathik.schoolpro

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import java.util.regex.Pattern


class SchoolProApp:Application() {


    override fun onCreate() {
        super.onCreate()



        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder()
            .name("schoolDB.realm")
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)


        Realm.init(applicationContext)

        Stetho.initialize(
            Stetho.newInitializerBuilder(applicationContext)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(applicationContext))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(applicationContext).build())
                .build())

        RealmInspectorModulesProvider.builder(this)
            .withFolder(cacheDir)
            .withMetaTables()
            .withDescendingOrder()
            .withLimit(1000)
            .databaseNamePattern(Pattern.compile(".+\\.realm"))
            .build()

      /*  val realmInspector = RealmInspectorModulesProvider.builder(this)
            .build()

        Stetho.initialize(Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(realmInspector)
            .build())*/

    }
}