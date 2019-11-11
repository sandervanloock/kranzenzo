import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';
import { IHomepageSetting } from 'app/shared/model/homepagesettings.model';
import { SERVER_API_URL } from 'app/app.constants';

type EntityResponseType = HttpResponse<IHomepageSetting>;

@Injectable({
    providedIn: 'root'
})
export class HomepageService {
    private resourceUrl = SERVER_API_URL + 'api/homepage-settings';

    constructor(private http: HttpClient) {}

    create(homepageSetting: IHomepageSetting): Observable<EntityResponseType> {
        return this.http.put<IHomepageSetting>(this.resourceUrl, homepageSetting, { observe: 'response' });
    }

    query(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHomepageSetting>(this.resourceUrl, { params: options, observe: 'response' });
    }
}
