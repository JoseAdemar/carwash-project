import { Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { VehicleComponent } from './components/vehicle/vehicle.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
    {path: '', component: LoginComponent},
    { path: 'app-home', component: HomeComponent},
    { path: 'app-customer', component: CustomerComponent},
    { path: 'app-vehicle', component: VehicleComponent},
   
];

/*@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
*/