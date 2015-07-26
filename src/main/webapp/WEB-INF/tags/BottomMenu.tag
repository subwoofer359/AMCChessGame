<%@ tag language="java" pageEncoding="UTF-8"%>
<nav role="navigation" class="navbar navbar-default navbar-fixed-bottom visible-xs visible-sm">
 <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            
            
            <a href="#" class="navbar-brand"></a>
        </div>
        <!-- Collection of nav links and other content for toggling -->
        <div id="navbarCollapse" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
               <jsp:doBody/>
            </ul>
        </div>
    </div>
</nav>