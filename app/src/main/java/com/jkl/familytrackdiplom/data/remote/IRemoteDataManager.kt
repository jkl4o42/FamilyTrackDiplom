package com.jkl.familytrack.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.jkl.familytrack.data.remote.model.family.Family

interface IRemoteDataManager {
    suspend fun writeOnFamily(model: Any, documentReference: DocumentReference, success:Any, failure: Any, navigator:Any)
    suspend fun createFamily(model:Any, documentReference: DocumentReference, success:Any, failure:Any, navigator: Any)
    suspend fun isDocumentExist(documentReference: DocumentReference,isExist:Any,notExist:Any,navigator: Any)
    suspend fun retriveFamily(documentReference: DocumentReference,isExist:Any,notExist:Any,navigator: Any)
    suspend fun retriveFamilyMembers(collectionReference: CollectionReference,familyId:String,membersRetrieved:Any,membersNotRetrieved:Any,navigator: Any)
    suspend fun setCurrentUserLocation(modelData:Any, documentReference: DocumentReference, userLocationSetSuccess:Any, userLocationSetFailure:Any, navigator: Any)
    suspend fun listenForFamilyMemberLocations(documentReferences: ArrayList<DocumentReference>, listenSuccess:Any, listenFailure:Any, navigator: Any)
    suspend fun retriveSingleMember(documentReference: DocumentReference,isExist:Any,notExist:Any,navigator: Any)
}