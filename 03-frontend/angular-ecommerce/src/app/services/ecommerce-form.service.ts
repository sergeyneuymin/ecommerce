import { HttpClient } from '@angular/common/http';
import { createMayBeForwardRefExpression } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Country } from '../common/country';
import { State } from '../common/state';

@Injectable({
  providedIn: 'root'
})
export class EcommerceFormService {

private countriesUrl = environment.ecommerceApiUrl + '/countries';
private statesUrl = environment.ecommerceApiUrl + '/states';

  constructor(private httpClient: HttpClient) { }

  getCountries(): Observable<Country[]> {
    return this.httpClient.get<GetResponseCountries>(this.countriesUrl).pipe(
      map(response => response._embedded.countries)
    );
  }

  getStates(theCountryCode: string): Observable<State[]> {
    const searchStatesUrl = `${this.statesUrl}/search/findByCountryCode?code=${theCountryCode}`;
    return this.httpClient.get<GetResponseStates>(this.statesUrl).pipe(
      map(response => response._embedded.states)
    );
  }

  getCreditCardMonths(startMonth: number): Observable<number[]> {
    let data: number[] = [];

    //build an array for month dropdown list
    // start at current month and loop until
    for(let theMonth = startMonth; theMonth <= 12; theMonth++){
      data.push(theMonth);
    }
    return of(data);
  }

  getCreditCardYears(): Observable<number[]> {
    let data: number[] = [];

    //build an array for year dropdown list
    // start at current year and loop for next 10 years
    const startYear: number = new Date().getFullYear();

    const endYear: number = startYear + 10;

    for(let theYear = startYear; theYear <= endYear; theYear++) {
      data.push(theYear);
    }
    return of(data);
  }
}

  interface GetResponseCountries {
    _embedded: {
      countries: Country[];
    }
  }

  interface GetResponseStates {
    _embedded: {
      states: State[];
    }
  }
