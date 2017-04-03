% Author: Marcel Stefko
% Copyright © 2017 Laboratory of Experimental Biophysics,
% École Polytechnique Fédérale de Lausanne,
% marcel.stefko@epfl.ch
 
% This file is part of STORMsim, a software package for simulating 
% fluorescent microscopy data.
%
% Several parts of this package's core were adapted under GPL from SOFIsim,
% a package developed by Arik Girsault and Tomas Lukes of Laboratoire 
% d'Optique Biomédicale at EPFL. <http://lob.epfl.ch/>
%
% STORMsim is free software: you can redistribute it and/or modify
% it under the terms of the GNU General Public License as published by
% the Free Software Foundation, either version 3 of the License, or
% (at your option) any later version.
%
% STORMsim is distributed in the hope that it will be useful,
% but WITHOUT ANY WARRANTY; without even the implied warranty of
% MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
% GNU General Public License for more details.
%
% You should have received a copy of the GNU General Public License
% along with STORMsim.  If not, see <http://www.gnu.org/licenses/>.
clear all; close all;


% Either give UI dialogs for file selection, or hardcoded values
loadByDialogs = false;
if loadByDialogs
    algorithm_csv = fullfile(pathname,filename);
else
    algorithm_csv = 'C:\\Users\\stefko\\Documents\\sim9\\tester_output.csv';
end

% Print the file locations
algorithm_csv

% open and parse the algorithm tester csv
alg_file = fopen(algorithm_csv,'r');
fgetl(alg_file); % discard first line
header_algorithm_settings = fgetl(alg_file)
fgetl(alg_file); % discard third line
header_algorithms = fgetl(alg_file);

algorithm_data = csvread(algorithm_csv,4,0);

x = algorithm_data(:,1);

% min, mean, and p10 from algorithm tester csv for SpotCounter
distance_measured_data = algorithm_data(:,[7 8 9]);
% normalize so that the mean distance along the whole measurement is 1
d_measured = movmean(distance_measured_data ./ mean(distance_measured_data(:,2)),25);

% plot SpotCounter performance against the real thing
figure(1)
plot(x,d_measured,'.')
legend('spot-min','spot-mean','spot-p10')
xlabel('Frame no. [-]')
ylabel('Emitter neighbor distance [normalized]')
title('SpotCounter output')


% Other algorithms
header = strsplit(header_algorithms,',');

% Normalize each column so it has a mean of 1
normalized_data = normalizeColumnsByMean(algorithm_data);

% Run a moving mean with width of 25 to smooth it out
movmean_data = movmean(normalized_data, 25);

% Plot the figure
figure(2)
yyaxis left
plot(x,movmean_data(:,[2,4,6,10]))
legend(header{[2,4,6,10]})
xlabel('Frame no. [-]')
ylabel('Count [-]')

yyaxis right
plot(x,algorithm_data(:,3));
ylabel('Laser power')
title('Active fluorophore count for different algorithms (smoothed by moving mean, width: 25 frames)')