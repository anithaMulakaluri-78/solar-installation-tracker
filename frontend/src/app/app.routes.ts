import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ConsumersComponent } from './pages/consumers/consumers.component';
import { InstallationsComponent } from './pages/installations/installations.component';
import { InstallationDetailComponent } from './pages/installation-detail/installation-detail.component';
import { DcrComponent } from './pages/dcr/dcr.component';
import { ShellComponent } from './layout/shell.component';
export const routes: Routes = [
 {path:'login',component:LoginComponent},
 {path:'',canActivate:[authGuard],component:ShellComponent,children:[
   {path:'dashboard',component:DashboardComponent},
   {path:'consumers',component:ConsumersComponent},
   {path:'installations',component:InstallationsComponent},
   {path:'installations/new',component:InstallationDetailComponent},
   {path:'installations/:id',component:InstallationDetailComponent},
   {path:'dcr',component:DcrComponent},
   {path:'',pathMatch:'full',redirectTo:'dashboard'}
 ]},
 {path:'**',redirectTo:''}
];
